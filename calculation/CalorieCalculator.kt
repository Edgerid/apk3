package com.personalfitnessos.core.calculation

import com.personalfitnessos.data.local.entity.ActivityLevel
import com.personalfitnessos.data.local.entity.CalorieTargetMethod
import com.personalfitnessos.data.local.entity.GoalDirection
import com.personalfitnessos.data.local.entity.Sex
import kotlin.math.roundToInt

/**
 * Every field the explanation screen (spec §6) needs to show its work: BMR, the activity
 * multiplier applied, the goal adjustment applied, and the resulting target. Nothing here
 * is hidden inside a single opaque number — that's the whole point of §6/§48's "transparent
 * calculations > mysterious scores" principle.
 */
data class CalorieBreakdown(
    val bmr: Int,
    val activityLevel: ActivityLevel,
    val tdee: Int,
    val goalDirection: GoalDirection,
    val goalAdjustmentKcal: Int,
    val finalTargetKcal: Int,
    val method: CalorieTargetMethod,
)

object CalorieCalculator {

    /**
     * Mifflin-St Jeor. For [Sex.UNSPECIFIED] (spec §5 requires sex to stay optional), the
     * male (+5) and female (-161) constants are averaged to -78 rather than guessing —
     * this is clearly an estimate either way, and averaging avoids silently assuming one
     * sex over the other when the user chose not to say.
     */
    fun calculateBmr(weightKg: Double, heightCm: Double, ageYears: Int, sex: Sex?): Int {
        val base = 10.0 * weightKg + 6.25 * heightCm - 5.0 * ageYears
        val sexConstant = when (sex) {
            Sex.MALE -> 5.0
            Sex.FEMALE -> -161.0
            Sex.UNSPECIFIED, null -> -78.0
        }
        return (base + sexConstant).roundToInt()
    }

    fun activityMultiplier(level: ActivityLevel): Double = when (level) {
        ActivityLevel.SEDENTARY -> 1.2
        ActivityLevel.LIGHT -> 1.375
        ActivityLevel.MODERATE -> 1.55
        ActivityLevel.ACTIVE -> 1.725
        ActivityLevel.VERY_ACTIVE -> 1.9
    }

    fun calculateTdee(bmr: Int, activityLevel: ActivityLevel): Int =
        (bmr * activityMultiplier(activityLevel)).roundToInt()

    /**
     * A conservative default adjustment (~0.45 kg/week) rather than an aggressive one —
     * the user can always override the final number, but the default should not encourage
     * an unsustainable deficit/surplus. CUSTOM defers entirely to whatever the user typed
     * into tdeeGoalAdjustmentKcal.
     */
    fun defaultGoalAdjustmentKcal(direction: GoalDirection, customAdjustment: Int?): Int =
        when (direction) {
            GoalDirection.LOSE -> -500
            GoalDirection.GAIN -> 300
            GoalDirection.MAINTAIN -> 0
            GoalDirection.CUSTOM -> customAdjustment ?: 0
        }

    /**
     * Resolves the final daily calorie target for whichever method the user picked (§6).
     * FIXED and MANUAL both just pass a user-entered number straight through — the
     * "calculation" for those methods is that there isn't one, which is itself worth
     * showing plainly on the explanation screen rather than hiding.
     */
    fun resolve(
        method: CalorieTargetMethod,
        fixedCalorieTarget: Int?,
        manualCalorieTarget: Int?,
        weightKg: Double?,
        heightCm: Double?,
        ageYears: Int?,
        sex: Sex?,
        activityLevel: ActivityLevel?,
        goalDirection: GoalDirection?,
        customGoalAdjustmentKcal: Int?,
    ): CalorieBreakdown? {
        return when (method) {
            CalorieTargetMethod.FIXED -> {
                val target = fixedCalorieTarget ?: return null
                CalorieBreakdown(0, ActivityLevel.SEDENTARY, 0, GoalDirection.MAINTAIN, 0, target, method)
            }
            CalorieTargetMethod.MANUAL -> {
                val target = manualCalorieTarget ?: return null
                CalorieBreakdown(0, ActivityLevel.SEDENTARY, 0, GoalDirection.MAINTAIN, 0, target, method)
            }
            CalorieTargetMethod.TDEE_ESTIMATE -> {
                if (weightKg == null || heightCm == null || ageYears == null ||
                    activityLevel == null || goalDirection == null
                ) return null
                val bmr = calculateBmr(weightKg, heightCm, ageYears, sex)
                val tdee = calculateTdee(bmr, activityLevel)
                val adjustment = defaultGoalAdjustmentKcal(goalDirection, customGoalAdjustmentKcal)
                CalorieBreakdown(
                    bmr = bmr,
                    activityLevel = activityLevel,
                    tdee = tdee,
                    goalDirection = goalDirection,
                    goalAdjustmentKcal = adjustment,
                    finalTargetKcal = (tdee + adjustment).coerceAtLeast(1000),
                    method = method,
                )
            }
        }
    }
}
