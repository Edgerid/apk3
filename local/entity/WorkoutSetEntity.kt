package com.personalfitnessos.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Only one of [rpe] / [rir] is normally filled at a time (they measure the same thing from
 * opposite ends); both are kept nullable and independent so the app never invents one from
 * the other. [completed] lets a planned set exist before it's actually performed, which the
 * active workout screen relies on for "current sets" editing (spec §14).
 */
@Entity(
    tableName = "workout_set",
    foreignKeys = [
        ForeignKey(
            entity = WorkoutExerciseEntity::class,
            parentColumns = ["id"],
            childColumns = ["workoutExerciseId"],
            onDelete = ForeignKey.CASCADE,
        )
    ],
    indices = [Index("workoutExerciseId")]
)
data class WorkoutSetEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val workoutExerciseId: Long,
    val setNumber: Int,
    val setType: SetType = SetType.NORMAL,
    val weightKg: Double?,
    val reps: Int?,
    val rpe: Double?,
    val rir: Int?,
    val completed: Boolean = false,
    val notes: String?,
)
