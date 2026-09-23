package com.personalfitnessos.data.repository

import com.personalfitnessos.data.local.dao.UserProfileDao
import com.personalfitnessos.data.local.entity.UserProfileEntity
import kotlinx.coroutines.flow.Flow

/**
 * Thin wrapper around [UserProfileDao]. Exists as its own class (rather than exposing the
 * DAO directly to ViewModels) so onboarding/profile-edit logic has one place to live if it
 * ever needs more than a pass-through — e.g. stamping updatedAt — without UI code knowing
 * about Room at all (spec §2: "UI must never directly access Room").
 */
class UserProfileRepository(private val dao: UserProfileDao) {
    fun observeProfile(): Flow<UserProfileEntity?> = dao.observe()

    suspend fun getProfile(): UserProfileEntity? = dao.get()

    suspend fun saveProfile(profile: UserProfileEntity) {
        dao.upsert(profile)
    }
}
