package com.personalfitnessos.data.repository

import android.content.Context
import com.personalfitnessos.data.local.AppDatabase

/**
 * Single hand-built composition root for every repository (spec §2's "lightweight DI,
 * avoid overengineering"). Constructed once in PersonalFitnessApp.onCreate and handed to
 * ViewModels via ViewModelFactory. Adding a new repository means adding one line here and
 * one constructor parameter — no annotation processing, no reflection, nothing hidden.
 */
class RepositoryProvider(
    @Suppress("unused") private val appContext: Context,
    database: AppDatabase,
) {
    val userProfile = UserProfileRepository(database.userProfileDao())

    val goals = GoalRepository(database.goalDao())

    val nutrition = NutritionRepository(
        foodDao = database.foodDao(),
        foodServingDao = database.foodServingDao(),
        foodLogDao = database.foodLogDao(),
        recipeDao = database.recipeDao(),
        recipeIngredientDao = database.recipeIngredientDao(),
        dailyTargetDao = database.dailyTargetDao(),
    )

    val workout = WorkoutRepository(
        exerciseDao = database.exerciseDao(),
        routineDao = database.workoutRoutineDao(),
        routineExerciseDao = database.routineExerciseDao(),
        sessionDao = database.workoutSessionDao(),
        workoutExerciseDao = database.workoutExerciseDao(),
        setDao = database.workoutSetDao(),
        personalRecordDao = database.personalRecordDao(),
    )

    val body = BodyRepository(
        weightDao = database.weightEntryDao(),
        measurementDao = database.bodyMeasurementDao(),
        photoDao = database.progressPhotoDao(),
        attendanceDao = database.gymAttendanceDao(),
    )

    val settings = SettingsRepository(database.appSettingDao())
}
