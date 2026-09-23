package com.personalfitnessos.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.personalfitnessos.data.local.entity.FoodServingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodServingDao {
    @Query("SELECT * FROM food_serving WHERE foodId = :foodId ORDER BY isDefault DESC, label")
    fun observeForFood(foodId: Long): Flow<List<FoodServingEntity>>

    @Insert
    suspend fun insert(serving: FoodServingEntity): Long

    @Delete
    suspend fun delete(serving: FoodServingEntity)
}
