package com.personalfitnessos.data.repository

import com.personalfitnessos.data.local.dao.GoalDao
import com.personalfitnessos.data.local.entity.GoalEntity
import com.personalfitnessos.data.local.entity.GoalStatus
import kotlinx.coroutines.flow.Flow

class GoalRepository(private val dao: GoalDao) {
    fun observeActive(): Flow<List<GoalEntity>> = dao.observeByStatus(GoalStatus.ACTIVE)
    fun observeAll(): Flow<List<GoalEntity>> = dao.observeAll()
    suspend fun getById(id: Long): GoalEntity? = dao.getById(id)
    suspend fun save(goal: GoalEntity): Long =
        if (goal.id == 0L) dao.insert(goal) else { dao.update(goal); goal.id }
    suspend fun delete(goal: GoalEntity) = dao.delete(goal)
}
