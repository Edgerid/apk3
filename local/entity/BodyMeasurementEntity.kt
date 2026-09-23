package com.personalfitnessos.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.Instant
import java.time.LocalDate

/**
 * [measurementType] holds one of the built-in names (waist, chest, arm, thigh, neck) or the
 * literal "custom"; when custom, [customLabel] carries the user's own name for it. Using one
 * flexible table instead of dedicated columns lets §20's "allow custom measurements"
 * requirement work without a schema migration every time a user wants to track something new.
 */
@Entity(
    tableName = "body_measurement",
    indices = [Index("logDate"), Index("measurementType")]
)
data class BodyMeasurementEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val logDate: LocalDate,
    val measurementType: String,
    val customLabel: String?,
    val valueCm: Double,
    val loggedAt: Instant,
    val notes: String?,
)
