package com.hong7.coinnews.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.hong7.coinnews.database.entity.CoinEntity
import com.hong7.coinnews.model.Coin
import kotlinx.coroutines.flow.Flow

@Dao
interface CoinDao {

    @Insert
    fun insertAll(list: List<CoinEntity>)

    @Query("SELECT (SELECT COUNT(*) FROM coin) == 0")
    suspend fun isEmpty(): Boolean

    @Query("SELECT * FROM coin WHERE name LIKE '%' || :query || '%'")
    fun getCoinsByName(query: String): Flow<List<CoinEntity>>
}