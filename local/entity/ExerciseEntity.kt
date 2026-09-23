package com.personalfitnessos.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "exercise",
    indices = [Index("name"), Index("category"), Index("isCustom")]
)
data class ExerciseEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val category: String,
    /** Comma-separated muscle names; simple and adequate for a single-user local app. */
    val primaryMuscles: String,
    val secondaryMuscles: String?,
    val equipment: String?,
    val movementType: MovementType,
    val instructions: String?,
    val isCustom: Boolean = false,
    val notes: String?,
)
