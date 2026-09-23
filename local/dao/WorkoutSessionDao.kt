package com.personalfitnessos.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.personalfitnessos.data.local.entity.WorkoutSessionEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface WorkoutSessionDao {
    /** Backs active-workout-recovery (spec §42): at most one row should ever have isActive=1. */
    @Query("SELECT * FROM workout_session WHERE isActive = 1 LIMIT 1")
    suspend fun getActiveSession(): WorkoutSessionEntity?

    @Query("SELECT * FROM workout_session WHERE isActive = 1 LIMIT 1")
    fun observeActiveSession(): Flow<WorkoutSessionEntity?>

    @Query("SELECT * FROM workout_session WHERE id = :id")
    suspend fun getById(id: Long): WorkoutSessionEntity?

    @Query("SELECT * FROM workout_session WHERE id = :id")
    fun observeById(id: Long): Flow<WorkoutSessionEntity?>

    @Query(
        "SELECT * FROM workout_session WHERE isActive = 0 " +
            "ORDER BY startedAt DESC LIMIT :limit OFFSET :offset"
    )
    suspend fun getRecentFinished(limit: Int, offset: Int): List<WorkoutSessionEntity>

    @Query("SELECT * FROM workout_session WHERE logDate BETWEEN :start AND :end ORDER BY startedAt")
    fun observeForRange(start: LocalDate, end: LocalDate): Flow<List<WorkoutSessionEntity>>

    @Insert
    suspend fun insert(session: WorkoutSessionEntity): Long

    @Update
    suspend fun update(session: WorkoutSessionEntity)

    @Delete
    suspend fun delete(session: WorkoutSessionEntity)
}
