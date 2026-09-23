package com.personalfitnessos

import android.app.Application
import com.personalfitnessos.data.local.AppDatabase
import com.personalfitnessos.data.repository.RepositoryProvider

/**
 * Application entry point. Owns the single Room database instance and the single
 * RepositoryProvider instance for the process lifetime. There is no dependency-injection
 * framework here on purpose (section 2 of the spec asks for a lightweight, maintainable
 * approach, not Hilt/Dagger) — everything below is constructed once, by hand, and handed
 * to ViewModels via a simple factory. This keeps the whole data-access graph visible in
 * one file instead of spread across annotations.
 */
class PersonalFitnessApp : Application() {

    lateinit var database: AppDatabase
        private set

    lateinit var repositories: RepositoryProvider
        private set

    override fun onCreate() {
        super.onCreate()
        database = AppDatabase.getInstance(this)
        repositories = RepositoryProvider(this, database)
    }
}
