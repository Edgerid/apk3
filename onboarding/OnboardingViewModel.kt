package com.personalfitnessos.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.personalfitnessos.core.calculation.CalorieCalculator
import com.personalfitnessos.core.calculation.MacroCalculator
import com.personalfitnessos.core.time.AppClock
import com.personalfitnessos.data.local.entity.ActivityLevel
import com.personalfitnessos.data.local.entity.CalorieTargetMethod
import com.personalfitnessos.data.local.entity.DailyTargetEntity
import com.personalfitnessos.data.local.entity.GoalDirection
import com.personalfitnessos.data.local.entity.Sex
import com.personalfitnessos.data.local.entity.UserProfileEntity
import com.personalfitnessos.data.repository.NutritionRepository
import com.personalfitnessos.data.repository.UserProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Onboarding never forces a calculated target on the user (spec §5/§6): whatever
 * [CalorieCalculator] estimates is only ever a *starting point* stored into
 * fixedCalorieTarget-equivalent fields, and the user's chosen [CalorieTargetMethod]
 * decides whether that estimate, a fixed number, or a fully manual number is what's
 * actually used day to day.
 */
class OnboardingViewModel(
    private val userProfileRepository: UserProfileRepository,
    private val nutritionRepository: NutritionRepository,
    private val clock: AppClock,
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    fun updateNickname(value: String) = _uiState.update { it.copy(nickname = value) }
    fun updateAge(value: String) = _uiState.update { it.copy(ageText = value) }
    fun updateSex(value: Sex?) = _uiState.update { it.copy(sex = value) }
    fun updateHeightCm(value: String) = _uiState.update { it.copy(heightCmText = value) }
    fun updateCurrentWeightKg(value: String) = _uiState.update { it.copy(currentWeightKgText = value) }
    fun updateTargetWeightKg(value: String) = _uiState.update { it.copy(targetWeightKgText = value) }
    fun updateGoalDirection(value: GoalDirection) = _uiState.update { it.copy(goalDirection = value) }
    fun updateActivityLevel(value: ActivityLevel) = _uiState.update { it.copy(activityLevel = value) }
    fun updateCalorieMethod(value: CalorieTargetMethod) =
        _uiState.update { it.copy(calorieTargetMethod = value) }
    fun updateFixedOrManualCalorie(value: String) =
        _uiState.update { it.copy(fixedOrManualCalorieText = value) }
    fun updateGymTargetSessions(value: String) =
        _uiState.update { it.copy(gymTargetSessionsPerWeekText = value) }

    /** A preview of what TDEE-based estimation would produce right now, for the explanation
     * screen (spec §6) — recomputed live as the user edits fields, never persisted until save. */
    fun previewBreakdown() = with(_uiState.value) {
        CalorieCalculator.resolve(
            method = CalorieTargetMethod.TDEE_ESTIMATE,
            fixedCalorieTarget = null,
            manualCalorieTarget = null,
            weightKg = currentWeightKgText.toDoubleOrNull(),
            heightCm = heightCmText.toDoubleOrNull(),
            ageYears = ageText.toIntOrNull(),
            sex = sex,
            activityLevel = activityLevel,
            goalDirection = goalDirection,
            customGoalAdjustmentKcal = null,
        )
    }

    fun save() {
        val state = _uiState.value
        val weightKg = state.currentWeightKgText.toDoubleOrNull()
        val heightCm = state.heightCmText.toDoubleOrNull()
        val ageYears = state.ageText.toIntOrNull()
        val manualOrFixed = state.fixedOrManualCalorieText.toIntOrNull()

        val breakdown = CalorieCalculator.resolve(
            method = state.calorieTargetMethod,
            fixedCalorieTarget = if (state.calorieTargetMethod == CalorieTargetMethod.FIXED) manualOrFixed else null,
            manualCalorieTarget = if (state.calorieTargetMethod == CalorieTargetMethod.MANUAL) manualOrFixed else null,
            weightKg = weightKg,
            heightCm = heightCm,
            ageYears = ageYears,
            sex = state.sex,
            activityLevel = state.activityLevel,
            goalDirection = state.goalDirection,
            customGoalAdjustmentKcal = null,
        )

        if (breakdown == null) {
            _uiState.update {
                it.copy(errorMessage = "Fill in weight, height, and age (or enter a target directly) to continue.")
            }
            return
        }

        val macros = MacroCalculator.defaultTargets(
            calorieTarget = breakdown.finalTargetKcal,
            bodyWeightKg = weightKg ?: 70.0,
        )

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, errorMessage = null) }
            val now = clock.now()
            val profile = UserProfileEntity(
                nickname = state.nickname.ifBlank { null },
                ageYears = ageYears,
                sex = state.sex,
                heightCm = heightCm,
                currentWeightKg = weightKg,
                targetWeightKg = state.targetWeightKgText.toDoubleOrNull(),
                goalDirection = state.goalDirection,
                activityLevel = state.activityLevel,
                calorieTargetMethod = state.calorieTargetMethod,
                fixedCalorieTarget = if (state.calorieTargetMethod == CalorieTargetMethod.FIXED) manualOrFixed else null,
                manualCalorieTarget = if (state.calorieTargetMethod == CalorieTargetMethod.MANUAL) manualOrFixed else null,
                tdeeGoalAdjustmentKcal = breakdown.goalAdjustmentKcal,
                proteinTargetGrams = macros.proteinGrams,
                carbsTargetGrams = macros.carbsGrams,
                fatTargetGrams = macros.fatGrams,
                fiberTargetGrams = macros.fiberGrams,
                gymTargetSessionsPerWeek = state.gymTargetSessionsPerWeekText.toIntOrNull(),
                onboardingCompleted = true,
                createdAt = now,
                updatedAt = now,
            )
            userProfileRepository.saveProfile(profile)

            nutritionRepository.setDailyTarget(
                DailyTargetEntity(
                    logDate = clock.today(),
                    calorieTarget = breakdown.finalTargetKcal,
                    proteinTargetGrams = macros.proteinGrams,
                    carbsTargetGrams = macros.carbsGrams,
                    fatTargetGrams = macros.fatGrams,
                    fiberTargetGrams = macros.fiberGrams,
                    calorieToleranceKcal = 100,
                )
            )

            _uiState.update { it.copy(isSaving = false, saved = true) }
        }
    }
}
