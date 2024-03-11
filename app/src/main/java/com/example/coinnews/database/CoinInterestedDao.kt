package com.example.coinnews.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface CoinInterestedDao {

    @Transaction
    @Query("SELECT * FROM coin_Interested")
    fun getCoinInterestedList(): Flow<List<CoinEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM coin_interested WHERE coin_id = :coinId LIMIT 1)")
    fun isInterested(coinId: String): Flow<Boolean>

    @Insert
    suspend fun insertCoinInterested(coin: CoinEntity)

    @Query("DELETE FROM coin_interested WHERE coin_id = :coinId")
    suspend fun deleteCoinInterested(coinId: String)
}