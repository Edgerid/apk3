package com.personalfitnessos.core.calculation

import kotlin.math.roundToInt

data class MacroTargets(
    val proteinGrams: Int,
    val carbsGrams: Int,
    val fatGrams: Int,
    val fiberGrams: Int,
)

/**
 * Produces a starting-point macro split; every value is a plain, disclosed default the user
 * can override (spec §7 "Allow manual macro targets" / §6 "Allow manual override" for the
 * same reason). Nothing here is trained or fitted — it's the standard general-population
 * ranges spelled out below, kept as named constants so anyone reading this can see exactly
 * what the app assumes.
 */
object MacroCalculator {
    private const val PROTEIN_G_PER_KG = 1.8
    private const val FAT_PERCENT_OF_CALORIES = 0.25
    private const val FIBER_G_PER_1000_KCAL = 14.0

    private const val KCAL_PER_G_PROTEIN = 4
    private const val KCAL_PER_G_CARB = 4
    private const val KCAL_PER_G_FAT = 9

    fun defaultTargets(calorieTarget: Int, bodyWeightKg: Double): MacroTargets {
        val proteinGrams = (bodyWeightKg * PROTEIN_G_PER_KG).roundToInt()
        val fatCalories = calorieTarget * FAT_PERCENT_OF_CALORIES
        val fatGrams = (fatCalories / KCAL_PER_G_FAT).roundToInt()

        val proteinCalories = proteinGrams * KCAL_PER_G_PROTEIN
        val remainingCalories = (calorieTarget - proteinCalories - fatCalories).coerceAtLeast(0.0)
        val carbsGrams = (remainingCalories / KCAL_PER_G_CARB).roundToInt()

        val fiberGrams = (calorieTarget / 1000.0 * FIBER_G_PER_1000_KCAL).roundToInt()

        return MacroTargets(proteinGrams, carbsGrams, fatGrams, fiberGrams)
    }
}
