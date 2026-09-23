package com.personalfitnessos.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.personalfitnessos.data.local.entity.FoodEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodDao {
    @Query(
        "SELECT * FROM food WHERE name LIKE '%' || :query || '%' " +
            "OR brand LIKE '%' || :query || '%' ORDER BY name LIMIT 50"
    )
    fun search(query: String): Flow<List<FoodEntity>>

    @Query("SELECT * FROM food WHERE isFavorite = 1 ORDER BY name")
    fun observeFavorites(): Flow<List<FoodEntity>>

    @Query("SELECT * FROM food WHERE barcode = :barcode LIMIT 1")
    suspend fun findByBarcode(barcode: String): FoodEntity?

    @Query("SELECT * FROM food WHERE id = :id")
    suspend fun getById(id: Long): FoodEntity?

    @Query("SELECT * FROM food WHERE id = :id")
    fun observeById(id: Long): Flow<FoodEntity?>

    @Insert
    suspend fun insert(food: FoodEntity): Long

    @Update
    suspend fun update(food: FoodEntity)

    @Delete
    suspend fun delete(food: FoodEntity)

    /**
     * Recently-used foods, derived from food_log rather than a separate "recent" table
     * (spec §9's "Add recent food") — one row per distinct food, most recent use first.
     */
    @Query(
        """
        SELECT f.* FROM food f
        WHERE f.id IN (
            SELECT DISTINCT foodId FROM food_log
            WHERE foodId IS NOT NULL
            ORDER BY loggedAt DESC
            LIMIT 200
        )
        ORDER BY (
            SELECT MAX(loggedAt) FROM food_log WHERE food_log.foodId = f.id
        ) DESC
        LIMIT 20
        """
    )
    fun observeRecentlyUsed(): Flow<List<FoodEntity>>
}
