package com.personalfitnessos.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.personalfitnessos.data.local.converter.Converters
import com.personalfitnessos.data.local.dao.AppSettingDao
import com.personalfitnessos.data.local.dao.BodyMeasurementDao
import com.personalfitnessos.data.local.dao.DailyTargetDao
import com.personalfitnessos.data.local.dao.ExerciseDao
import com.personalfitnessos.data.local.dao.FoodDao
import com.personalfitnessos.data.local.dao.FoodLogDao
import com.personalfitnessos.data.local.dao.FoodServingDao
import com.personalfitnessos.data.local.dao.GoalDao
import com.personalfitnessos.data.local.dao.GymAttendanceDao
import com.personalfitnessos.data.local.dao.PersonalRecordDao
import com.personalfitnessos.data.local.dao.ProgressPhotoDao
import com.personalfitnessos.data.local.dao.RecipeDao
import com.personalfitnessos.data.local.dao.RecipeIngredientDao
import com.personalfitnessos.data.local.dao.RoutineExerciseDao
import com.personalfitnessos.data.local.dao.UserProfileDao
import com.personalfitnessos.data.local.dao.WeightEntryDao
import com.personalfitnessos.data.local.dao.WorkoutExerciseDao
import com.personalfitnessos.data.local.dao.WorkoutRoutineDao
import com.personalfitnessos.data.local.dao.WorkoutSessionDao
import com.personalfitnessos.data.local.dao.WorkoutSetDao
import com.personalfitnessos.data.local.entity.AppSettingEntity
import com.personalfitnessos.data.local.entity.BodyMeasurementEntity
import com.personalfitnessos.data.local.entity.DailyTargetEntity
import com.personalfitnessos.data.local.entity.ExerciseEntity
import com.personalfitnessos.data.local.entity.FoodEntity
import com.personalfitnessos.data.local.entity.FoodLogEntity
import com.personalfitnessos.data.local.entity.FoodServingEntity
import com.personalfitnessos.data.local.entity.GoalEntity
import com.personalfitnessos.data.local.entity.GymAttendanceEntity
import com.personalfitnessos.data.local.entity.PersonalRecordEntity
import com.personalfitnessos.data.local.entity.ProgressPhotoEntity
import com.personalfitnessos.data.local.entity.RecipeEntity
import com.personalfitnessos.data.local.entity.RecipeIngredientEntity
import com.personalfitnessos.data.local.entity.RoutineExerciseEntity
import com.personalfitnessos.data.local.entity.UserProfileEntity
import com.personalfitnessos.data.local.entity.WeightEntryEntity
import com.personalfitnessos.data.local.entity.WorkoutExerciseEntity
import com.personalfitnessos.data.local.entity.WorkoutRoutineEntity
import com.personalfitnessos.data.local.entity.WorkoutSessionEntity
import com.personalfitnessos.data.local.entity.WorkoutSetEntity

/**
 * Schema version 1. Every future schema change MUST add a Migration in [AppDatabaseMigrations]
 * and register it in [com.personalfitnessos.core.di.AppContainer] — never
 * fallbackToDestructiveMigration(), per spec §35's "never destroy user data during schema
 * changes." exportSchema is on so schemas/ JSON files can be checked into version control and
 * diffed, which is what makes writing a correct migration for the *next* version possible.
 */
@Database(
    entities = [
        UserProfileEntity::class,
        GoalEntity::class,
        FoodEntity::class,
        FoodServingEntity::class,
        FoodLogEntity::class,
        RecipeEntity::class,
        RecipeIngredientEntity::class,
        ExerciseEntity::class,
        WorkoutRoutineEntity::class,
        RoutineExerciseEntity::class,
        WorkoutSessionEntity::class,
        WorkoutExerciseEntity::class,
        WorkoutSetEntity::class,
        GymAttendanceEntity::class,
        WeightEntryEntity::class,
        BodyMeasurementEntity::class,
        ProgressPhotoEntity::class,
        PersonalRecordEntity::class,
        DailyTargetEntity::class,
        AppSettingEntity::class,
    ],
    version = 1,
    exportSchema = true,
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userProfileDao(): UserProfileDao
    abstract fun goalDao(): GoalDao
    abstract fun foodDao(): FoodDao
    abstract fun foodServingDao(): FoodServingDao
    abstract fun foodLogDao(): FoodLogDao
    abstract fun recipeDao(): RecipeDao
    abstract fun recipeIngredientDao(): RecipeIngredientDao
    abstract fun exerciseDao(): ExerciseDao
    abstract fun workoutRoutineDao(): WorkoutRoutineDao
    abstract fun routineExerciseDao(): RoutineExerciseDao
    abstract fun workoutSessionDao(): WorkoutSessionDao
    abstract fun workoutExerciseDao(): WorkoutExerciseDao
    abstract fun workoutSetDao(): WorkoutSetDao
    abstract fun gymAttendanceDao(): GymAttendanceDao
    abstract fun weightEntryDao(): WeightEntryDao
    abstract fun bodyMeasurementDao(): BodyMeasurementDao
    abstract fun progressPhotoDao(): ProgressPhotoDao
    abstract fun personalRecordDao(): PersonalRecordDao
    abstract fun dailyTargetDao(): DailyTargetDao
    abstract fun appSettingDao(): AppSettingDao

    companion object {
        const val DATABASE_NAME = "personal_fitness_os.db"

        @Volatile
        private var instance: AppDatabase? = null

        /**
         * Standard double-checked-locking singleton. There is exactly one Room instance
         * for the process lifetime, held by [com.personalfitnessos.PersonalFitnessApp] —
         * this getInstance exists so tests and previews can also obtain a database without
         * going through the Application class.
         */
        fun getInstance(context: Context): AppDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME,
                ).addMigrations(*AppDatabaseMigrations.ALL)
                    .build()
                    .also { instance = it }
            }
    }
}
