package com.personalfitnessos.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.personalfitnessos.data.local.entity.WorkoutRoutineEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutRoutineDao {
    @Query("SELECT * FROM workout_routine ORDER BY name")
    fun observeAll(): Flow<List<WorkoutRoutineEntity>>

    @Query("SELECT * FROM workout_routine WHERE id = :id")
    suspend fun getById(id: Long): WorkoutRoutineEntity?

    @Query("SELECT * FROM workout_routine WHERE id = :id")
    fun observeById(id: Long): Flow<WorkoutRoutineEntity?>

    @Insert
    suspend fun insert(routine: WorkoutRoutineEntity): Long

    @Update
    suspend fun update(routine: WorkoutRoutineEntity)

    @Delete
    suspend fun delete(routine: WorkoutRoutineEntity)
}
