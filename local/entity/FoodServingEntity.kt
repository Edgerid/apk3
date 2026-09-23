package com.personalfitnessos.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * A [FoodEntity] always has its implicit base serving (servingSize/servingUnit on the food
 * itself). This table holds additional named servings a user or a data source may define for
 * the same food — e.g. "1 slice = 28 g", "1 scoop = 32 g" — so food entry (spec §9) can offer
 * a friendly picker instead of forcing users to weigh everything in grams.
 */
@Entity(
    tableName = "food_serving",
    foreignKeys = [
        ForeignKey(
            entity = FoodEntity::class,
            parentColumns = ["id"],
            childColumns = ["foodId"],
            onDelete = ForeignKey.CASCADE,
        )
    ],
    indices = [Index("foodId")]
)
data class FoodServingEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val foodId: Long,
    val label: String,
    val gramsEquivalent: Double,
    val isDefault: Boolean = false,
)
