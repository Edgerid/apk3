package com.personalfitnessos.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.Instant

/**
 * Not in the spec's minimum entity list (§35) but required to satisfy §18 and §26 without
 * recomputing PR history from every workout set on every screen open. Rows here are fully
 * derived data: recalculated whenever the source WorkoutSetEntity rows they reference
 * change (an edited or deleted set triggers PR recalculation for that exercise), never
 * hand-edited, and never deleted just because a workout was edited — see §18's "recalculate
 * consistently from source data" rule.
 */
@Entity(
    tableName = "personal_record",
    foreignKeys = [
        ForeignKey(
            entity = ExerciseEntity::class,
            parentColumns = ["id"],
            childColumns = ["exerciseId"],
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = WorkoutSetEntity::class,
            parentColumns = ["id"],
            childColumns = ["sourceSetId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("exerciseId"), Index("type"), Index("achievedAt")]
)
data class PersonalRecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val exerciseId: Long,
    val type: PrType,
    /** For REPS_AT_WEIGHT, the weight the reps were achieved at; null for other types. */
    val atWeightKg: Double?,
    val value: Double,
    val sourceSetId: Long,
    val achievedAt: Instant,
)
