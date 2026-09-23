package com.personalfitnessos.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.personalfitnessos.data.local.entity.GymAttendanceEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface GymAttendanceDao {
    /** An open visit (checkOut still null) — at most one should exist at a time. */
    @Query("SELECT * FROM gym_attendance WHERE checkOut IS NULL ORDER BY checkIn DESC LIMIT 1")
    suspend fun getOpenVisit(): GymAttendanceEntity?

    @Query("SELECT * FROM gym_attendance WHERE checkOut IS NULL ORDER BY checkIn DESC LIMIT 1")
    fun observeOpenVisit(): Flow<GymAttendanceEntity?>

    @Query("SELECT * FROM gym_attendance WHERE logDate BETWEEN :start AND :end ORDER BY checkIn")
    fun observeForRange(start: LocalDate, end: LocalDate): Flow<List<GymAttendanceEntity>>

    @Query("SELECT * FROM gym_attendance ORDER BY checkIn DESC")
    fun observeAll(): Flow<List<GymAttendanceEntity>>

    @Query("SELECT * FROM gym_attendance WHERE id = :id")
    suspend fun getById(id: Long): GymAttendanceEntity?

    /** Distinct visit dates, most recent first — the raw feed streak calculation runs over. */
    @Query(
        "SELECT DISTINCT logDate FROM gym_attendance WHERE checkOut IS NOT NULL ORDER BY logDate DESC"
    )
    suspend fun getDistinctVisitDatesDesc(): List<LocalDate>

    @Insert
    suspend fun insert(visit: GymAttendanceEntity): Long

    @Update
    suspend fun update(visit: GymAttendanceEntity)

    @Delete
    suspend fun delete(visit: GymAttendanceEntity)
}
