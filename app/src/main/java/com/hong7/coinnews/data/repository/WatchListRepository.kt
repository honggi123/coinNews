package com.hong7.coinnews.data.repository

import com.hong7.coinnews.model.Coin
import kotlinx.coroutines.flow.Flow

interface WatchListRepository {

   fun getWatchList(): Flow<List<Coin>>

    suspend fun addWatchListCoin(coin: Coin)

    suspend fun removeWatchListCoin(coin: Coin)
}