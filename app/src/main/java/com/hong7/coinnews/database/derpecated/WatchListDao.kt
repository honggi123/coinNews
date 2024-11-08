//package com.hong7.coinnews.database.dao
//
//import androidx.room.Dao
//import androidx.room.Delete
//import androidx.room.Insert
//import androidx.room.Query
//import com.hong7.coinnews.database.entity.WatchListCoinEntity
//import kotlinx.coroutines.flow.Flow
//
//@Dao
//interface WatchListDao {
//
//    @Query("SELECT * FROM watchlist ORDER BY rank DESC")
//    fun getWatchListCoin(): Flow<List<WatchListCoinEntity>>
//
//    @Insert
//    suspend fun addWatchListCoin(coin: WatchListCoinEntity)
//
//    @Query("DELETE FROM watchlist WHERE id = :coinId")
//    suspend fun removeWatchListCoin(coinId: String)
//}