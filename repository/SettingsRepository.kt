package com.personalfitnessos.data.repository

import com.personalfitnessos.data.local.dao.AppSettingDao
import com.personalfitnessos.data.local.entity.AppSettingEntity
import kotlinx.coroutines.flow.Flow

/**
 * Generic key/value settings backed by the app_setting table (spec §35). Most UI-facing
 * preferences (theme, rest timer default) live in DataStore instead — see
 * core/di/AppContainer — this repository is for settings that must be part of the
 * JSON export/import (spec §30), such as the Health Connect connection flag and the
 * calorie-adherence tolerance.
 */
class SettingsRepository(private val dao: AppSettingDao) {
    suspend fun get(key: String): String? = dao.get(key)?.value
    fun observeAll(): Flow<List<AppSettingEntity>> = dao.observeAll()
    suspend fun set(key: String, value: String) = dao.upsert(AppSettingEntity(key, value))
    suspend fun clear(key: String) = dao.delete(key)

    companion object {
        const val KEY_HEALTH_CONNECT_ENABLED = "health_connect_enabled"
        const val KEY_CALORIE_TOLERANCE_KCAL = "calorie_tolerance_kcal"
        const val KEY_DEFAULT_REST_SECONDS = "default_rest_seconds"
    }
}
