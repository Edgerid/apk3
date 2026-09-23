package com.personalfitnessos.data.repository

import com.personalfitnessos.data.local.dao.ExerciseDao
import com.personalfitnessos.data.local.dao.PersonalRecordDao
import com.personalfitnessos.data.local.dao.RoutineExerciseDao
import com.personalfitnessos.data.local.dao.WorkoutExerciseDao
import com.personalfitnessos.data.local.dao.WorkoutRoutineDao
import com.personalfitnessos.data.local.dao.WorkoutSessionDao
import com.personalfitnessos.data.local.dao.WorkoutSetDao
import com.personalfitnessos.data.local.entity.ExerciseEntity
import com.personalfitnessos.data.local.entity.PersonalRecordEntity
import com.personalfitnessos.data.local.entity.PrType
import com.personalfitnessos.data.local.entity.RoutineExerciseEntity
import com.personalfitnessos.data.local.entity.WorkoutExerciseEntity
import com.personalfitnessos.data.local.entity.WorkoutRoutineEntity
import com.personalfitnessos.data.local.entity.WorkoutSessionEntity
import com.personalfitnessos.data.local.entity.WorkoutSetEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

/**
 * Covers the exercise library, routines, active/historical workout sessions, sets, and
 * personal records. This is the busiest repository in the app because the active-workout
 * screen (spec §14, "the most important screen") needs all of these together in real time.
 */
class WorkoutRepository(
    private val exerciseDao: ExerciseDao,
    private val routineDao: WorkoutRoutineDao,
    private val routineExerciseDao: RoutineExerciseDao,
    private val sessionDao: WorkoutSessionDao,
    private val workoutExerciseDao: WorkoutExerciseDao,
    private val setDao: WorkoutSetDao,
    private val personalRecordDao: PersonalRecordDao,
) {
    // --- Exercise library ---
    fun observeExercises(): Flow<List<ExerciseEntity>> = exerciseDao.observeAll()
    fun observeExercisesByCategory(category: String): Flow<List<ExerciseEntity>> =
        exerciseDao.observeByCategory(category)
    fun searchExercises(query: String): Flow<List<ExerciseEntity>> = exerciseDao.search(query)
    suspend fun getExercise(id: Long): ExerciseEntity? = exerciseDao.getById(id)
    suspend fun exerciseCount(): Int = exerciseDao.count()
    suspend fun seedExercises(exercises: List<ExerciseEntity>) = exerciseDao.insertAll(exercises)
    suspend fun saveExercise(exercise: ExerciseEntity): Long =
        if (exercise.id == 0L) exerciseDao.insert(exercise) else {
            exerciseDao.update(exercise); exercise.id
        }
    suspend fun deleteExercise(exercise: ExerciseEntity) = exerciseDao.delete(exercise)

    // --- Routines ---
    fun observeRoutines(): Flow<List<WorkoutRoutineEntity>> = routineDao.observeAll()
    suspend fun getRoutine(id: Long): WorkoutRoutineEntity? = routineDao.getById(id)
    fun observeRoutineExercises(routineId: Long): Flow<List<RoutineExerciseEntity>> =
        routineExerciseDao.observeForRoutine(routineId)

    suspend fun saveRoutine(
        routine: WorkoutRoutineEntity,
        exercises: List<RoutineExerciseEntity>,
    ): Long {
        val routineId = if (routine.id == 0L) routineDao.insert(routine) else {
            routineDao.update(routine); routine.id
        }
        routineExerciseDao.deleteAllForRoutine(routineId)
        routineExerciseDao.insertAll(exercises.map { it.copy(routineId = routineId) })
        return routineId
    }

    suspend fun deleteRoutine(routine: WorkoutRoutineEntity) = routineDao.delete(routine)

    /** Duplicates a routine and its exercise list as a new independent routine (spec §13). */
    suspend fun duplicateRoutine(routine: WorkoutRoutineEntity): Long {
        val copy = routine.copy(id = 0, name = "${routine.name} (copy)")
        val originalExercises = routineExerciseDao.getForRoutine(routine.id)
        return saveRoutine(copy, originalExercises.map { it.copy(id = 0) })
    }

    // --- Active workout ---
    suspend fun getActiveSession(): WorkoutSessionEntity? = sessionDao.getActiveSession()
    fun observeActiveSession(): Flow<WorkoutSessionEntity?> = sessionDao.observeActiveSession()
    fun observeSession(id: Long): Flow<WorkoutSessionEntity?> = sessionDao.observeById(id)
    suspend fun startSession(session: WorkoutSessionEntity): Long = sessionDao.insert(session)
    suspend fun updateSession(session: WorkoutSessionEntity) = sessionDao.update(session)
    suspend fun discardSession(session: WorkoutSessionEntity) = sessionDao.delete(session)

    fun observeWorkoutExercises(sessionId: Long): Flow<List<WorkoutExerciseEntity>> =
        workoutExerciseDao.observeForSession(sessionId)
    suspend fun addWorkoutExercise(exercise: WorkoutExerciseEntity): Long =
        workoutExerciseDao.insert(exercise)
    suspend fun findPreviousPerformance(
        exerciseId: Long,
        excludingSessionId: Long,
    ): WorkoutExerciseEntity? = workoutExerciseDao.findMostRecentPrevious(exerciseId, excludingSessionId)

    fun observeSets(workoutExerciseId: Long): Flow<List<WorkoutSetEntity>> =
        setDao.observeForWorkoutExercise(workoutExerciseId)
    suspend fun getSetsForWorkoutExercise(workoutExerciseId: Long): List<WorkoutSetEntity> =
        setDao.getForWorkoutExercise(workoutExerciseId)
    suspend fun addSet(set: WorkoutSetEntity): Long = setDao.insert(set)
    suspend fun updateSet(set: WorkoutSetEntity) = setDao.update(set)
    suspend fun deleteSet(set: WorkoutSetEntity) = setDao.delete(set)

    // --- History ---
    suspend fun getRecentFinishedSessions(limit: Int, offset: Int = 0): List<WorkoutSessionEntity> =
        sessionDao.getRecentFinished(limit, offset)
    fun observeSessionsForRange(start: LocalDate, end: LocalDate): Flow<List<WorkoutSessionEntity>> =
        sessionDao.observeForRange(start, end)
    suspend fun getCompletedSetHistory(exerciseId: Long): List<WorkoutSetEntity> =
        setDao.getCompletedHistoryForExercise(exerciseId)

    // --- Personal records ---
    fun observePersonalRecords(exerciseId: Long): Flow<List<PersonalRecordEntity>> =
        personalRecordDao.observeForExercise(exerciseId)
    fun observeRecentPersonalRecords(limit: Int): Flow<List<PersonalRecordEntity>> =
        personalRecordDao.observeRecent(limit)
    suspend fun getBestRecord(exerciseId: Long, type: PrType): PersonalRecordEntity? =
        personalRecordDao.getBest(exerciseId, type)

    /** Full recalculation for one exercise — see PrCalculator in core/calculation. */
    suspend fun replacePersonalRecords(exerciseId: Long, records: List<PersonalRecordEntity>) {
        personalRecordDao.deleteAllForExercise(exerciseId)
        personalRecordDao.insertAll(records)
    }
}
