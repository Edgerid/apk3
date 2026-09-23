package com.personalfitnessos.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.personalfitnessos.data.local.entity.WorkoutSetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutSetDao {
    @Query("SELECT * FROM workout_set WHERE workoutExerciseId = :workoutExerciseId ORDER BY setNumber")
    fun observeForWorkoutExercise(workoutExerciseId: Long): Flow<List<WorkoutSetEntity>>

    @Query("SELECT * FROM workout_set WHERE workoutExerciseId = :workoutExerciseId ORDER BY setNumber")
    suspend fun getForWorkoutExercise(workoutExerciseId: Long): List<WorkoutSetEntity>

    @Query("SELECT * FROM workout_set WHERE id = :id")
    suspend fun getById(id: Long): WorkoutSetEntity?

    /**
     * All completed sets belonging to a given exercise (across all sessions), oldest first.
     * Used by the PR-detection and volume-history calculators (§17/§18) — kept as a raw
     * feed rather than pre-aggregated so those calculators can recompute deterministically
     * from source data whenever a set is edited or deleted, per §18's rule.
     */
    @Query(
        """
        SELECT s.* FROM workout_set s
        INNER JOIN workout_exercise we ON we.id = s.workoutExerciseId
        INNER JOIN workout_session ws ON ws.id = we.sessionId
        WHERE we.exerciseId = :exerciseId AND s.completed = 1 AND ws.isActive = 0
        ORDER BY ws.startedAt ASC
        """
    )
    suspend fun getCompletedHistoryForExercise(exerciseId: Long): List<WorkoutSetEntity>

    @Insert
    suspend fun insert(set: WorkoutSetEntity): Long

    @Update
    suspend fun update(set: WorkoutSetEntity)

    @Delete
    suspend fun delete(set: WorkoutSetEntity)
}
