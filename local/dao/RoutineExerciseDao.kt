package com.personalfitnessos.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.personalfitnessos.data.local.entity.RoutineExerciseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RoutineExerciseDao {
    @Query("SELECT * FROM routine_exercise WHERE routineId = :routineId ORDER BY sortOrder")
    fun observeForRoutine(routineId: Long): Flow<List<RoutineExerciseEntity>>

    @Query("SELECT * FROM routine_exercise WHERE routineId = :routineId ORDER BY sortOrder")
    suspend fun getForRoutine(routineId: Long): List<RoutineExerciseEntity>

    @Insert
    suspend fun insert(entry: RoutineExerciseEntity): Long

    @Insert
    suspend fun insertAll(entries: List<RoutineExerciseEntity>)

    @Update
    suspend fun update(entry: RoutineExerciseEntity)

    @Delete
    suspend fun delete(entry: RoutineExerciseEntity)

    @Query("DELETE FROM routine_exercise WHERE routineId = :routineId")
    suspend fun deleteAllForRoutine(routineId: Long)
}
