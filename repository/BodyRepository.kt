package com.personalfitnessos.data.repository

import com.personalfitnessos.data.local.dao.BodyMeasurementDao
import com.personalfitnessos.data.local.dao.GymAttendanceDao
import com.personalfitnessos.data.local.dao.ProgressPhotoDao
import com.personalfitnessos.data.local.dao.WeightEntryDao
import com.personalfitnessos.data.local.entity.BodyMeasurementEntity
import com.personalfitnessos.data.local.entity.GymAttendanceEntity
import com.personalfitnessos.data.local.entity.PhotoCategory
import com.personalfitnessos.data.local.entity.ProgressPhotoEntity
import com.personalfitnessos.data.local.entity.WeightEntryEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

/** Covers body weight, measurements, progress photos, and gym attendance/check-in. */
class BodyRepository(
    private val weightDao: WeightEntryDao,
    private val measurementDao: BodyMeasurementDao,
    private val photoDao: ProgressPhotoDao,
    private val attendanceDao: GymAttendanceDao,
) {
    // --- Weight ---
    fun observeWeightForRange(start: LocalDate, end: LocalDate): Flow<List<WeightEntryEntity>> =
        weightDao.observeForRange(start, end)
    fun observeLatestWeight(): Flow<WeightEntryEntity?> = weightDao.observeLatest()
    suspend fun logWeight(entry: WeightEntryEntity) = weightDao.upsert(entry)
    suspend fun deleteWeight(entry: WeightEntryEntity) = weightDao.delete(entry)

    // --- Body measurements ---
    fun observeMeasurementsForType(type: String): Flow<List<BodyMeasurementEntity>> =
        measurementDao.observeForType(type)
    fun observeMeasurementsForRange(
        start: LocalDate,
        end: LocalDate,
    ): Flow<List<BodyMeasurementEntity>> = measurementDao.observeForRange(start, end)
    fun observeMeasurementTypes(): Flow<List<String>> = measurementDao.observeDistinctTypes()
    suspend fun logMeasurement(measurement: BodyMeasurementEntity): Long =
        measurementDao.insert(measurement)
    suspend fun deleteMeasurement(measurement: BodyMeasurementEntity) = measurementDao.delete(measurement)

    // --- Progress photos ---
    fun observePhotos(): Flow<List<ProgressPhotoEntity>> = photoDao.observeAll()
    fun observePhotosByCategory(category: PhotoCategory): Flow<List<ProgressPhotoEntity>> =
        photoDao.observeByCategory(category)
    suspend fun addPhoto(photo: ProgressPhotoEntity): Long = photoDao.insert(photo)
    suspend fun deletePhoto(photo: ProgressPhotoEntity) = photoDao.delete(photo)

    // --- Gym attendance ---
    suspend fun getOpenVisit(): GymAttendanceEntity? = attendanceDao.getOpenVisit()
    fun observeOpenVisit(): Flow<GymAttendanceEntity?> = attendanceDao.observeOpenVisit()
    fun observeAttendanceForRange(start: LocalDate, end: LocalDate): Flow<List<GymAttendanceEntity>> =
        attendanceDao.observeForRange(start, end)
    fun observeAllAttendance(): Flow<List<GymAttendanceEntity>> = attendanceDao.observeAll()
    suspend fun checkIn(visit: GymAttendanceEntity): Long = attendanceDao.insert(visit)
    suspend fun checkOut(visit: GymAttendanceEntity) = attendanceDao.update(visit)
    suspend fun deleteVisit(visit: GymAttendanceEntity) = attendanceDao.delete(visit)
    suspend fun getDistinctVisitDatesDesc(): List<LocalDate> = attendanceDao.getDistinctVisitDatesDesc()
}
