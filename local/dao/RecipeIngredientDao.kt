package com.personalfitnessos.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.personalfitnessos.data.local.entity.RecipeIngredientEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeIngredientDao {
    @Query("SELECT * FROM recipe_ingredient WHERE recipeId = :recipeId ORDER BY sortOrder")
    fun observeForRecipe(recipeId: Long): Flow<List<RecipeIngredientEntity>>

    @Query("SELECT * FROM recipe_ingredient WHERE recipeId = :recipeId ORDER BY sortOrder")
    suspend fun getForRecipe(recipeId: Long): List<RecipeIngredientEntity>

    @Insert
    suspend fun insert(ingredient: RecipeIngredientEntity): Long

    @Insert
    suspend fun insertAll(ingredients: List<RecipeIngredientEntity>)

    @Update
    suspend fun update(ingredient: RecipeIngredientEntity)

    @Delete
    suspend fun delete(ingredient: RecipeIngredientEntity)

    @Query("DELETE FROM recipe_ingredient WHERE recipeId = :recipeId")
    suspend fun deleteAllForRecipe(recipeId: Long)
}
