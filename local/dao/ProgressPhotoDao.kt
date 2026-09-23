package com.personalfitnessos.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.personalfitnessos.data.local.entity.PhotoCategory
import com.personalfitnessos.data.local.entity.ProgressPhotoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProgressPhotoDao {
    @Query("SELECT * FROM progress_photo ORDER BY logDate DESC")
    fun observeAll(): Flow<List<ProgressPhotoEntity>>

    @Query("SELECT * FROM progress_photo WHERE category = :category ORDER BY logDate DESC")
    fun observeByCategory(category: PhotoCategory): Flow<List<ProgressPhotoEntity>>

    @Insert
    suspend fun insert(photo: ProgressPhotoEntity): Long

    @Delete
    suspend fun delete(photo: ProgressPhotoEntity)
}
