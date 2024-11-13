package com.hong7.coinnews.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hong7.coinnews.database.entity.CoinEntity
import com.hong7.coinnews.model.Coin
import kotlinx.coroutines.flow.Flow

@Dao
interface CoinDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<CoinEntity>)

    @Query("SELECT * FROM coin_table")
    fun getCoins(): Flow<List<CoinEntity>>

    @Query("DELETE FROM coin_table")
    fun deleteAllCoins()
//    @Query("SELECT * FROM coin WHERE name LIKE '%' || :query || '%'")
//    fun getCoinsByName(query: String): Flow<List<CoinEntity>>
}