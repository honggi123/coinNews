package com.hong7.coinnews.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserFilterDao {

    @Query("SELECT * FROM filter ORDER BY id DESC LIMIT 1")
    suspend fun getRecentFilter(): FilterEntity?

    @Query("SELECT * FROM filter WHERE id = :filterId")
    suspend fun getFilterById(filterId: String): FilterEntity

    @Query("SELECT (SELECT COUNT(*) FROM filter) == 0")
    suspend fun isEmpty(): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(filter: FilterEntity)

    @Query("DELETE FROM filter")
    suspend fun deleteFilter()
}