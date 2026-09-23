package com.personalfitnessos.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * foodId uses RESTRICT-like behavior via SET_NULL plus a stored [foodNameSnapshot], so a
 * recipe still displays sensibly even if the underlying food is later deleted; the
 * ingredient's own nutrition contribution was already baked into the recipe/food-log totals
 * at the time it mattered, per the same historical-accuracy rule as FoodLogEntity.
 */
@Entity(
    tableName = "recipe_ingredient",
    foreignKeys = [
        ForeignKey(
            entity = RecipeEntity::class,
            parentColumns = ["id"],
            childColumns = ["recipeId"],
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = FoodEntity::class,
            parentColumns = ["id"],
            childColumns = ["foodId"],
            onDelete = ForeignKey.SET_NULL,
        ),
    ],
    indices = [Index("recipeId"), Index("foodId")]
)
data class RecipeIngredientEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val recipeId: Long,
    val foodId: Long?,
    val foodNameSnapshot: String,
    val grams: Double,
    val sortOrder: Int,
)
