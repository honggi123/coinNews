package com.example.coinnews.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.coinnews.model.Filter
import kotlinx.coroutines.flow.Flow

@Dao
interface FilterDao {

    @Transaction
    @Query("SELECT * FROM filter")
    fun getAllFilters(): Flow<FilterEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(filter: FilterEntity)

    @Query("DELETE FROM filter")
    suspend fun deleteFilter()

    @Query("SELECT (SELECT COUNT(*) FROM filter) == 0")
    suspend fun isEmpty(): Boolean
}