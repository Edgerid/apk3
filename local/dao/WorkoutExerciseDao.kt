package com.personalfitnessos.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.personalfitnessos.data.local.entity.WorkoutExerciseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutExerciseDao {
    @Query("SELECT * FROM workout_exercise WHERE sessionId = :sessionId ORDER BY sortOrder")
    fun observeForSession(sessionId: Long): Flow<List<WorkoutExerciseEntity>>

    @Query("SELECT * FROM workout_exercise WHERE sessionId = :sessionId ORDER BY sortOrder")
    suspend fun getForSession(sessionId: Long): List<WorkoutExerciseEntity>

    @Query("SELECT * FROM workout_exercise WHERE id = :id")
    suspend fun getById(id: Long): WorkoutExerciseEntity?

    /**
     * Most recent past occurrence of this exercise, excluding the current (still-active)
     * session, used to populate "Previous: 60 kg x 8..." on the active workout screen (§14).
     */
    @Query(
        """
        SELECT we.* FROM workout_exercise we
        INNER JOIN workout_session ws ON ws.id = we.sessionId
        WHERE we.exerciseId = :exerciseId AND we.sessionId != :excludingSessionId
            AND ws.isActive = 0
        ORDER BY ws.startedAt DESC LIMIT 1
        """
    )
    suspend fun findMostRecentPrevious(
        exerciseId: Long,
        excludingSessionId: Long,
    ): WorkoutExerciseEntity?

    @Insert
    suspend fun insert(exercise: WorkoutExerciseEntity): Long

    @Insert
    suspend fun insertAll(exercises: List<WorkoutExerciseEntity>)

    @Update
    suspend fun update(exercise: WorkoutExerciseEntity)

    @Delete
    suspend fun delete(exercise: WorkoutExerciseEntity)
}
