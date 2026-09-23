package com.personalfitnessos.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(tableName = "workout_routine", indices = [Index("name")])
data class WorkoutRoutineEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val description: String?,
    val notes: String?,
    val createdAt: Instant,
    val updatedAt: Instant,
)
