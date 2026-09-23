package com.personalfitnessos.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.Instant

/**
 * Nutrition values are always stored per 100 g/ml, regardless of how the food is entered
 * or displayed. Any unit conversion (grams, cups, pieces, custom servings) happens at
 * calculation time in core/calculation, using [servingSize]/[servingUnit] as the reference
 * for "1 serving" — never by mutating these per-100 base values, so unit changes never
 * silently drift.
 */
@Entity(
    tableName = "food",
    indices = [Index("barcode"), Index("name"), Index("isFavorite")]
)
data class FoodEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val brand: String?,
    val barcode: String?,
    val caloriesPer100g: Double,
    val proteinPer100g: Double,
    val carbsPer100g: Double,
    val fatPer100g: Double,
    val fiberPer100g: Double,
    val sugarPer100g: Double,
    val sodiumMgPer100g: Double,
    val servingSize: Double,
    val servingUnit: MeasurementUnit,
    val customUnitLabel: String?,
    val source: DataSource,
    val verified: Boolean = false,
    val isFavorite: Boolean = false,
    val createdAt: Instant,
    val updatedAt: Instant,
)
