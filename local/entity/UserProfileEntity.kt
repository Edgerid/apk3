package com.personalfitnessos.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

/**
 * There is only ever one row here (id is always 1) — this is a single-user, local-only app.
 * Every field is nullable except id/createdAt/updatedAt because onboarding (spec §5)
 * explicitly must not force values the user doesn't want to give (e.g. sex is optional).
 */
@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Int = SINGLETON_ID,
    val nickname: String?,
    val ageYears: Int?,
    val sex: Sex?,
    val heightCm: Double?,
    val currentWeightKg: Double?,
    val targetWeightKg: Double?,
    val goalDirection: GoalDirection?,
    val activityLevel: ActivityLevel?,
    val calorieTargetMethod: CalorieTargetMethod?,
    val fixedCalorieTarget: Int?,
    val manualCalorieTarget: Int?,
    val tdeeGoalAdjustmentKcal: Int?,
    val proteinTargetGrams: Int?,
    val carbsTargetGrams: Int?,
    val fatTargetGrams: Int?,
    val fiberTargetGrams: Int?,
    val gymTargetSessionsPerWeek: Int?,
    val onboardingCompleted: Boolean = false,
    val weightUnitPreference: WeightUnitPreference = WeightUnitPreference.KG,
    val createdAt: Instant,
    val updatedAt: Instant,
) {
    companion object {
        const val SINGLETON_ID = 1
    }
}
