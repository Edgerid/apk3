package com.personalfitnessos.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.Instant
import java.time.LocalDate

/**
 * [isActive] backs the active-workout-recovery requirement (spec §42): if the app process
 * is killed mid-workout, the row for the in-progress session is still on disk with
 * isActive=true, so on next launch the app can offer "Resume" or "Discard" instead of
 * silently losing the workout.
 */
@Entity(
    tableName = "workout_session",
    foreignKeys = [
        ForeignKey(
            entity = WorkoutRoutineEntity::class,
            parentColumns = ["id"],
            childColumns = ["routineId"],
            onDelete = ForeignKey.SET_NULL,
        )
    ],
    indices = [Index("logDate"), Index("isActive"), Index("routineId")]
)
data class WorkoutSessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val routineId: Long?,
    val routineNameSnapshot: String?,
    val logDate: LocalDate,
    val startedAt: Instant,
    val finishedAt: Instant?,
    val isActive: Boolean = true,
    val notes: String?,
)
