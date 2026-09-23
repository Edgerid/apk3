package com.personalfitnessos.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.Duration
import java.time.Instant
import java.time.LocalDate

@Entity(
    tableName = "gym_attendance",
    foreignKeys = [
        ForeignKey(
            entity = WorkoutSessionEntity::class,
            parentColumns = ["id"],
            childColumns = ["workoutSessionId"],
            onDelete = ForeignKey.SET_NULL,
        )
    ],
    indices = [Index("logDate"), Index("workoutSessionId")]
)
data class GymAttendanceEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val logDate: LocalDate,
    val checkIn: Instant,
    val checkOut: Instant?,
    /** Stored in whole seconds, derived from (checkOut - checkIn) once checked out. */
    val durationSeconds: Long?,
    val workoutSessionId: Long?,
    val notes: String?,
) {
    companion object {
        fun computeDuration(checkIn: Instant, checkOut: Instant): Long =
            Duration.between(checkIn, checkOut).seconds.coerceAtLeast(0)
    }
}
