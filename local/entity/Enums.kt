package com.personalfitnessos.data.local.entity

/** Where a value came from — never let a calculated number pretend to be measured (spec §29). */
enum class DataSource { MANUAL, OPEN_FOOD_FACTS, HEALTH_CONNECT, IMPORTED, CALCULATED }

enum class Sex { MALE, FEMALE, UNSPECIFIED }

enum class GoalDirection { LOSE, MAINTAIN, GAIN, CUSTOM }

enum class ActivityLevel { SEDENTARY, LIGHT, MODERATE, ACTIVE, VERY_ACTIVE }

enum class CalorieTargetMethod { FIXED, TDEE_ESTIMATE, MANUAL }

enum class MeasurementUnit {
    GRAM, KILOGRAM, MILLILITER, LITER, PIECE, SERVING, CUP, TABLESPOON, TEASPOON, CUSTOM
}

enum class MealType { BREAKFAST, LUNCH, DINNER, SNACK, CUSTOM }

enum class MovementType { COMPOUND, ISOLATION, CARDIO, MOBILITY, OTHER }

enum class SetType { NORMAL, WARMUP, DROP_SET, FAILURE, SUPERSET }

enum class GoalCategory {
    BODY_WEIGHT, GYM_ATTENDANCE, CALORIE_ADHERENCE, PROTEIN_ADHERENCE, STRENGTH, CUSTOM
}

enum class GoalStatus { ACTIVE, COMPLETED, ABANDONED }

enum class PhotoCategory { FRONT, SIDE, BACK, CUSTOM }

enum class PrType { HEAVIEST_WEIGHT, REPS_AT_WEIGHT, ESTIMATED_ONE_REP_MAX, SESSION_VOLUME }

enum class WeightUnitPreference { KG, LB }
