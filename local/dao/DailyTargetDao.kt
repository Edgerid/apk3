package com.personalfitnessos.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.personalfitnessos.data.local.entity.DailyTargetEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface DailyTargetDao {
    @Query("SELECT * FROM daily_target WHERE logDate = :date LIMIT 1")
    suspend fun getForDate(date: LocalDate): DailyTargetEntity?

    @Query("SELECT * FROM daily_target WHERE logDate = :date LIMIT 1")
    fun observeForDate(date: LocalDate): Flow<DailyTargetEntity?>

    @Query("SELECT * FROM daily_target WHERE logDate BETWEEN :start AND :end ORDER BY logDate")
    fun observeForRange(start: LocalDate, end: LocalDate): Flow<List<DailyTargetEntity>>

    /** One row per day (unique index on logDate) — a same-day target change replaces it. */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(target: DailyTargetEntity): Long
}
