package com.personalfitnessos.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate

/**
 * A resolved snapshot of what a given day's calorie/macro targets actually were, written
 * once when the day starts (or when the user changes their target mid-day). This is what
 * makes calorie-adherence (§24) and history (§27) meaningful after the user later changes
 * their profile's target: today's adherence is always judged against the target that was
 * actually in effect that day, not today's current setting.
 */
@Entity(tableName = "daily_target", indices = [Index("logDate", unique = true)])
data class DailyTargetEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val logDate: LocalDate,
    val calorieTarget: Int,
    val proteinTargetGrams: Int,
    val carbsTargetGrams: Int,
    val fatTargetGrams: Int,
    val fiberTargetGrams: Int,
    /** +/- kcal treated as "within tolerance" for calorie-adherence calculations (§24). */
    val calorieToleranceKcal: Int,
)
