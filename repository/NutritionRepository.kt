package com.personalfitnessos.data.repository

import com.personalfitnessos.data.local.dao.DailyNutritionTotals
import com.personalfitnessos.data.local.dao.DailyTargetDao
import com.personalfitnessos.data.local.dao.FoodDao
import com.personalfitnessos.data.local.dao.FoodLogDao
import com.personalfitnessos.data.local.dao.FoodServingDao
import com.personalfitnessos.data.local.dao.RecipeDao
import com.personalfitnessos.data.local.dao.RecipeIngredientDao
import com.personalfitnessos.data.local.entity.DailyTargetEntity
import com.personalfitnessos.data.local.entity.FoodEntity
import com.personalfitnessos.data.local.entity.FoodLogEntity
import com.personalfitnessos.data.local.entity.FoodServingEntity
import com.personalfitnessos.data.local.entity.RecipeEntity
import com.personalfitnessos.data.local.entity.RecipeIngredientEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

/**
 * Covers food, food servings, food logging, recipes/ingredients, and the resolved daily
 * target snapshot. Kept as one repository (rather than five) because nutrition screens
 * routinely need several of these together (e.g. logging food touches Food, FoodLog, and
 * potentially DailyTarget); splitting further would just move coordination into ViewModels
 * without reducing it.
 */
class NutritionRepository(
    private val foodDao: FoodDao,
    private val foodServingDao: FoodServingDao,
    private val foodLogDao: FoodLogDao,
    private val recipeDao: RecipeDao,
    private val recipeIngredientDao: RecipeIngredientDao,
    private val dailyTargetDao: DailyTargetDao,
) {
    // --- Food ---
    fun searchFood(query: String): Flow<List<FoodEntity>> = foodDao.search(query)
    fun observeFavoriteFoods(): Flow<List<FoodEntity>> = foodDao.observeFavorites()
    fun observeRecentlyUsedFoods(): Flow<List<FoodEntity>> = foodDao.observeRecentlyUsed()
    suspend fun findFoodByBarcode(barcode: String): FoodEntity? = foodDao.findByBarcode(barcode)
    suspend fun getFood(id: Long): FoodEntity? = foodDao.getById(id)
    fun observeFood(id: Long): Flow<FoodEntity?> = foodDao.observeById(id)
    suspend fun saveFood(food: FoodEntity): Long =
        if (food.id == 0L) foodDao.insert(food) else { foodDao.update(food); food.id }
    suspend fun deleteFood(food: FoodEntity) = foodDao.delete(food)

    // --- Food servings ---
    fun observeServingsForFood(foodId: Long): Flow<List<FoodServingEntity>> =
        foodServingDao.observeForFood(foodId)
    suspend fun addServing(serving: FoodServingEntity): Long = foodServingDao.insert(serving)
    suspend fun deleteServing(serving: FoodServingEntity) = foodServingDao.delete(serving)

    // --- Food log ---
    fun observeLogForDate(date: LocalDate): Flow<List<FoodLogEntity>> =
        foodLogDao.observeForDate(date)
    fun observeDailyTotals(date: LocalDate): Flow<DailyNutritionTotals> =
        foodLogDao.observeDailyTotals(date)
    suspend fun logFood(entry: FoodLogEntity): Long = foodLogDao.insert(entry)
    suspend fun updateLogEntry(entry: FoodLogEntity) = foodLogDao.update(entry)
    suspend fun deleteLogEntry(entry: FoodLogEntity) = foodLogDao.delete(entry)

    // --- Recipes ---
    fun observeRecipes(): Flow<List<RecipeEntity>> = recipeDao.observeAll()
    fun searchRecipes(query: String): Flow<List<RecipeEntity>> = recipeDao.search(query)
    suspend fun getRecipe(id: Long): RecipeEntity? = recipeDao.getById(id)
    fun observeRecipeIngredients(recipeId: Long): Flow<List<RecipeIngredientEntity>> =
        recipeIngredientDao.observeForRecipe(recipeId)

    /** Replaces all ingredients for a recipe atomically-enough for a single-user local app. */
    suspend fun saveRecipe(recipe: RecipeEntity, ingredients: List<RecipeIngredientEntity>): Long {
        val recipeId = if (recipe.id == 0L) recipeDao.insert(recipe) else {
            recipeDao.update(recipe)
            recipe.id
        }
        recipeIngredientDao.deleteAllForRecipe(recipeId)
        recipeIngredientDao.insertAll(ingredients.map { it.copy(recipeId = recipeId) })
        return recipeId
    }

    suspend fun deleteRecipe(recipe: RecipeEntity) = recipeDao.delete(recipe)

    // --- Daily targets ---
    suspend fun getDailyTarget(date: LocalDate): DailyTargetEntity? = dailyTargetDao.getForDate(date)
    fun observeDailyTarget(date: LocalDate): Flow<DailyTargetEntity?> =
        dailyTargetDao.observeForDate(date)
    suspend fun setDailyTarget(target: DailyTargetEntity) {
        dailyTargetDao.upsert(target)
    }
}
