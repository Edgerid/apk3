package com.personalfitnessos.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.personalfitnessos.data.local.entity.FoodLogEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

/** Aggregated totals for one day. Returned by Room's typed projection query below. */
data class DailyNutritionTotals(
    val calories: Double,
    val protein: Double,
    val carbs: Double,
    val fat: Double,
    val fiber: Double,
    val sugar: Double,
    val sodiumMg: Double,
)

@Dao
interface FoodLogDao {
    @Query("SELECT * FROM food_log WHERE logDate = :date ORDER BY loggedAt")
    fun observeForDate(date: LocalDate): Flow<List<FoodLogEntity>>

    @Query("SELECT * FROM food_log WHERE logDate BETWEEN :start AND :end ORDER BY loggedAt")
    fun observeForRange(start: LocalDate, end: LocalDate): Flow<List<FoodLogEntity>>

    @Query(
        """
        SELECT
            COALESCE(SUM(caloriesTotal), 0) AS calories,
            COALESCE(SUM(proteinTotal), 0) AS protein,
            COALESCE(SUM(carbsTotal), 0) AS carbs,
            COALESCE(SUM(fatTotal), 0) AS fat,
            COALESCE(SUM(fiberTotal), 0) AS fiber,
            COALESCE(SUM(sugarTotal), 0) AS sugar,
            COALESCE(SUM(sodiumMgTotal), 0) AS sodiumMg
        FROM food_log WHERE logDate = :date
        """
    )
    fun observeDailyTotals(date: LocalDate): Flow<DailyNutritionTotals>

    @Insert
    suspend fun insert(log: FoodLogEntity): Long

    @Update
    suspend fun update(log: FoodLogEntity)

    @Delete
    suspend fun delete(log: FoodLogEntity)

    @Query("SELECT * FROM food_log WHERE id = :id")
    suspend fun getById(id: Long): FoodLogEntity?
}
