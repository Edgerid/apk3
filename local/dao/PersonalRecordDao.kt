package com.personalfitnessos.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.personalfitnessos.data.local.entity.PersonalRecordEntity
import com.personalfitnessos.data.local.entity.PrType
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonalRecordDao {
    @Query("SELECT * FROM personal_record WHERE exerciseId = :exerciseId ORDER BY achievedAt DESC")
    fun observeForExercise(exerciseId: Long): Flow<List<PersonalRecordEntity>>

    @Query(
        "SELECT * FROM personal_record WHERE exerciseId = :exerciseId AND type = :type " +
            "ORDER BY value DESC LIMIT 1"
    )
    suspend fun getBest(exerciseId: Long, type: PrType): PersonalRecordEntity?

    @Query("SELECT * FROM personal_record ORDER BY achievedAt DESC LIMIT :limit")
    fun observeRecent(limit: Int): Flow<List<PersonalRecordEntity>>

    @Insert
    suspend fun insert(record: PersonalRecordEntity): Long

    @Insert
    suspend fun insertAll(records: List<PersonalRecordEntity>)

    @Delete
    suspend fun delete(record: PersonalRecordEntity)

    /**
     * Wipes and lets the caller reinsert PRs for one exercise — used when recalculating from
     * source data after a set edit/delete (§18), never as a user-facing "clear PRs" action.
     */
    @Query("DELETE FROM personal_record WHERE exerciseId = :exerciseId")
    suspend fun deleteAllForExercise(exerciseId: Long)
}
