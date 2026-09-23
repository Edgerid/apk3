package com.personalfitnessos.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.Instant
import java.time.LocalDate

/**
 * A logged food entry snapshots its own nutrition totals at the moment of logging
 * (caloriesTotal..sodiumMgTotal below), rather than recomputing from the live [FoodEntity]
 * or [RecipeEntity] every time history is viewed. This is required by spec §10/§29:
 * editing a food's nutrition or a recipe later must never retroactively change what a past
 * day's total looked like. foodId/recipeId are kept only as a reference for "log this again"
 * convenience and are nullable so a food can later be deleted without breaking history.
 *
 * [logDate] is the local calendar day this entry counts toward, captured explicitly at
 * entry time (see Converters doc comment) — never re-derived from [loggedAt] later.
 */
@Entity(
    tableName = "food_log",
    foreignKeys = [
        ForeignKey(
            entity = FoodEntity::class,
            parentColumns = ["id"],
            childColumns = ["foodId"],
            onDelete = ForeignKey.SET_NULL,
        ),
        ForeignKey(
            entity = RecipeEntity::class,
            parentColumns = ["id"],
            childColumns = ["recipeId"],
            onDelete = ForeignKey.SET_NULL,
        ),
    ],
    indices = [Index("logDate"), Index("mealType"), Index("foodId"), Index("recipeId")]
)
data class FoodLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val foodId: Long?,
    val recipeId: Long?,
    val displayName: String,
    val quantity: Double,
    val unit: MeasurementUnit,
    val gramsEquivalent: Double,
    val caloriesTotal: Double,
    val proteinTotal: Double,
    val carbsTotal: Double,
    val fatTotal: Double,
    val fiberTotal: Double,
    val sugarTotal: Double,
    val sodiumMgTotal: Double,
    val mealType: MealType,
    val logDate: LocalDate,
    val loggedAt: Instant,
    val source: DataSource = DataSource.MANUAL,
    val notes: String?,
)
