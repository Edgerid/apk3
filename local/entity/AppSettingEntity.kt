package com.personalfitnessos.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Generic key/value store for settings that don't warrant a dedicated typed column
 * (theme mode, rest-timer default seconds, calorie-tolerance for adherence, Health Connect
 * connection flag, etc.). Most of these are actually held in DataStore rather than here —
 * this table exists for settings that need to be queried relationally alongside other data,
 * or exported/imported as part of a JSON backup (spec §30).
 */
@Entity(tableName = "app_setting")
data class AppSettingEntity(
    @PrimaryKey val key: String,
    val value: String,
)
