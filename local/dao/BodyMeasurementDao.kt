package com.personalfitnessos.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.personalfitnessos.data.local.entity.BodyMeasurementEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface BodyMeasurementDao {
    @Query(
        "SELECT * FROM body_measurement WHERE measurementType = :type " +
            "ORDER BY logDate DESC"
    )
    fun observeForType(type: String): Flow<List<BodyMeasurementEntity>>

    @Query(
        "SELECT * FROM body_measurement WHERE logDate BETWEEN :start AND :end ORDER BY logDate"
    )
    fun observeForRange(start: LocalDate, end: LocalDate): Flow<List<BodyMeasurementEntity>>

    @Query("SELECT DISTINCT measurementType FROM body_measurement ORDER BY measurementType")
    fun observeDistinctTypes(): Flow<List<String>>

    @Insert
    suspend fun insert(measurement: BodyMeasurementEntity): Long

    @Update
    suspend fun update(measurement: BodyMeasurementEntity)

    @Delete
    suspend fun delete(measurement: BodyMeasurementEntity)
}
