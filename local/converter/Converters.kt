package com.personalfitnessos.data.local.converter

import androidx.room.TypeConverter
import com.personalfitnessos.data.local.entity.ActivityLevel
import com.personalfitnessos.data.local.entity.CalorieTargetMethod
import com.personalfitnessos.data.local.entity.DataSource
import com.personalfitnessos.data.local.entity.GoalCategory
import com.personalfitnessos.data.local.entity.GoalDirection
import com.personalfitnessos.data.local.entity.GoalStatus
import com.personalfitnessos.data.local.entity.MealType
import com.personalfitnessos.data.local.entity.MeasurementUnit
import com.personalfitnessos.data.local.entity.MovementType
import com.personalfitnessos.data.local.entity.PhotoCategory
import com.personalfitnessos.data.local.entity.PrType
import com.personalfitnessos.data.local.entity.SetType
import com.personalfitnessos.data.local.entity.Sex
import com.personalfitnessos.data.local.entity.WeightUnitPreference
import java.time.Instant
import java.time.LocalDate

/**
 * All timestamps are stored as epoch millis (UTC, [Instant]) — unambiguous across timezone
 * changes. All "which calendar day does this belong to" fields are stored separately as
 * ISO-8601 [LocalDate] strings, computed once in the user's local timezone at the moment of
 * entry (see core/time). This split is what section 36 of the spec requires: a meal logged
 * at 23:59 keeps the calendar day it was logged on even if the device's timezone changes
 * later, because the day was captured explicitly rather than re-derived from the instant.
 */
class Converters {

    @TypeConverter
    fun instantToEpochMillis(value: Instant?): Long? = value?.toEpochMilli()

    @TypeConverter
    fun epochMillisToInstant(value: Long?): Instant? = value?.let { Instant.ofEpochMilli(it) }

    @TypeConverter
    fun localDateToIso(value: LocalDate?): String? = value?.toString()

    @TypeConverter
    fun isoToLocalDate(value: String?): LocalDate? = value?.let { LocalDate.parse(it) }

    @TypeConverter
    fun dataSourceToString(value: DataSource?): String? = value?.name

    @TypeConverter
    fun stringToDataSource(value: String?): DataSource? = value?.let { DataSource.valueOf(it) }

    @TypeConverter
    fun sexToString(value: Sex?): String? = value?.name

    @TypeConverter
    fun stringToSex(value: String?): Sex? = value?.let { Sex.valueOf(it) }

    @TypeConverter
    fun goalDirectionToString(value: GoalDirection?): String? = value?.name

    @TypeConverter
    fun stringToGoalDirection(value: String?): GoalDirection? =
        value?.let { GoalDirection.valueOf(it) }

    @TypeConverter
    fun activityLevelToString(value: ActivityLevel?): String? = value?.name

    @TypeConverter
    fun stringToActivityLevel(value: String?): ActivityLevel? =
        value?.let { ActivityLevel.valueOf(it) }

    @TypeConverter
    fun calorieMethodToString(value: CalorieTargetMethod?): String? = value?.name

    @TypeConverter
    fun stringToCalorieMethod(value: String?): CalorieTargetMethod? =
        value?.let { CalorieTargetMethod.valueOf(it) }

    @TypeConverter
    fun unitToString(value: MeasurementUnit?): String? = value?.name

    @TypeConverter
    fun stringToUnit(value: String?): MeasurementUnit? =
        value?.let { MeasurementUnit.valueOf(it) }

    @TypeConverter
    fun mealTypeToString(value: MealType?): String? = value?.name

    @TypeConverter
    fun stringToMealType(value: String?): MealType? = value?.let { MealType.valueOf(it) }

    @TypeConverter
    fun movementTypeToString(value: MovementType?): String? = value?.name

    @TypeConverter
    fun stringToMovementType(value: String?): MovementType? =
        value?.let { MovementType.valueOf(it) }

    @TypeConverter
    fun setTypeToString(value: SetType?): String? = value?.name

    @TypeConverter
    fun stringToSetType(value: String?): SetType? = value?.let { SetType.valueOf(it) }

    @TypeConverter
    fun goalCategoryToString(value: GoalCategory?): String? = value?.name

    @TypeConverter
    fun stringToGoalCategory(value: String?): GoalCategory? =
        value?.let { GoalCategory.valueOf(it) }

    @TypeConverter
    fun goalStatusToString(value: GoalStatus?): String? = value?.name

    @TypeConverter
    fun stringToGoalStatus(value: String?): GoalStatus? = value?.let { GoalStatus.valueOf(it) }

    @TypeConverter
    fun photoCategoryToString(value: PhotoCategory?): String? = value?.name

    @TypeConverter
    fun stringToPhotoCategory(value: String?): PhotoCategory? =
        value?.let { PhotoCategory.valueOf(it) }

    @TypeConverter
    fun prTypeToString(value: PrType?): String? = value?.name

    @TypeConverter
    fun stringToPrType(value: String?): PrType? = value?.let { PrType.valueOf(it) }

    @TypeConverter
    fun weightUnitToString(value: WeightUnitPreference?): String? = value?.name

    @TypeConverter
    fun stringToWeightUnit(value: String?): WeightUnitPreference? =
        value?.let { WeightUnitPreference.valueOf(it) }
}
