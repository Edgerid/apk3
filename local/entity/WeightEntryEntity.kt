package com.personalfitnessos.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.Instant
import java.time.LocalDate

@Entity(tableName = "weight_entry", indices = [Index("logDate", unique = true)])
data class WeightEntryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val logDate: LocalDate,
    val weightKg: Double,
    val bodyFatPercent: Double?,
    val loggedAt: Instant,
    val source: DataSource = DataSource.MANUAL,
    val notes: String?,
)
