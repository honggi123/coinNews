package com.example.coinnews.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface CoinFilterDao {

    @Transaction
    @Query("SELECT * FROM coin_filter")
    fun getAllFilters(): Flow<List<CoinFilterEntity>>

    @Transaction
    @Query("SELECT * FROM coin_filter WHERE is_selected = :selected")
    fun getSelctedFilters(selected: Boolean = true): Flow<List<CoinFilterEntity>>

    @Insert
    suspend fun insertAllFilters(filters: List<CoinFilterEntity>)

    @Query("UPDATE coin_filter SET is_selected=:isSelected WHERE id = :id")
    suspend fun updateFilterSelect(id: String, isSelected: Boolean)

    @Query("DELETE FROM coin_filter")
    suspend fun deleteAllFilters()

    @Query("SELECT (SELECT COUNT(*) FROM coin_filter) == 0")
    suspend fun isEmpty(): Boolean

}