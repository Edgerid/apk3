package com.personalfitnessos.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.personalfitnessos.data.local.entity.WeightEntryEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface WeightEntryDao {
    @Query("SELECT * FROM weight_entry WHERE logDate BETWEEN :start AND :end ORDER BY logDate")
    fun observeForRange(start: LocalDate, end: LocalDate): Flow<List<WeightEntryEntity>>

    @Query("SELECT * FROM weight_entry ORDER BY logDate DESC LIMIT 1")
    fun observeLatest(): Flow<WeightEntryEntity?>

    @Query("SELECT * FROM weight_entry WHERE logDate = :date LIMIT 1")
    suspend fun getForDate(date: LocalDate): WeightEntryEntity?

    /** One entry per calendar day (unique index on logDate) — logging again that day edits it. */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entry: WeightEntryEntity): Long

    @Update
    suspend fun update(entry: WeightEntryEntity)

    @Delete
    suspend fun delete(entry: WeightEntryEntity)
}
