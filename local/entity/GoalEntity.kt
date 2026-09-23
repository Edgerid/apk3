package com.personalfitnessos.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.Instant
import java.time.LocalDate

@Entity(
    tableName = "goal",
    indices = [Index("status"), Index("category"), Index("targetDate")]
)
data class GoalEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val category: GoalCategory,
    val startValue: Double,
    val currentValue: Double,
    val targetValue: Double,
    val unit: String,
    val startDate: LocalDate,
    val targetDate: LocalDate?,
    val status: GoalStatus = GoalStatus.ACTIVE,
    val notes: String?,
    val linkedExerciseId: Long? = null,
    val createdAt: Instant,
    val updatedAt: Instant,
)
