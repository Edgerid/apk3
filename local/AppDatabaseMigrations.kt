package com.personalfitnessos.data.local

import androidx.room.migration.Migration

/**
 * Registry of Room migrations, oldest to newest. Empty at schema version 1 — the first
 * entry goes here the moment version 2 is needed. [AppDatabase] must never use
 * fallbackToDestructiveMigration(); every version bump gets a real Migration added to
 * [ALL] instead (spec §35).
 */
object AppDatabaseMigrations {
    val ALL: Array<Migration> = arrayOf()
}
