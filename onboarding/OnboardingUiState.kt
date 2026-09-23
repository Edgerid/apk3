package com.personalfitnessos.ui.onboarding

import com.personalfitnessos.data.local.entity.ActivityLevel
import com.personalfitnessos.data.local.entity.CalorieTargetMethod
import com.personalfitnessos.data.local.entity.GoalDirection
import com.personalfitnessos.data.local.entity.Sex

/**
 * All fields are strings/nullable at the UI edge because onboarding must never force a
 * value the user doesn't want to give (spec §5) — parsing/validation happens only when
 * [com.personalfitnessos.ui.onboarding.OnboardingViewModel.save] is called, and a field
 * left blank simply stays null in the saved profile.
 */
data class OnboardingUiState(
    val nickname: String = "",
    val ageText: String = "",
    val sex: Sex? = null,
    val heightCmText: String = "",
    val currentWeightKgText: String = "",
    val targetWeightKgText: String = "",
    val goalDirection: GoalDirection = GoalDirection.MAINTAIN,
    val activityLevel: ActivityLevel = ActivityLevel.MODERATE,
    val calorieTargetMethod: CalorieTargetMethod = CalorieTargetMethod.TDEE_ESTIMATE,
    val fixedOrManualCalorieText: String = "",
    val gymTargetSessionsPerWeekText: String = "3",
    val isSaving: Boolean = false,
    val errorMessage: String? = null,
    val saved: Boolean = false,
)
