package com.personalfitnessos.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.Instant
import java.time.LocalDate

/**
 * [filePath] points into this app's private storage (never a shared/public directory) —
 * spec §22/§31 require photos to stay local and never be uploaded automatically.
 */
@Entity(
    tableName = "progress_photo",
    indices = [Index("logDate"), Index("category")]
)
data class ProgressPhotoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val logDate: LocalDate,
    val category: PhotoCategory,
    val customLabel: String?,
    val filePath: String,
    val takenAt: Instant,
)
