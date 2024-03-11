package com.example.coinnews.data.repository

import androidx.paging.PagingData
import com.example.coinnews.model.Coin
import com.example.coinnews.model.Sort
import kotlinx.coroutines.flow.Flow

interface CoinRepository {

    fun isInterested(coinId: String): Flow<Boolean>

    fun getCoinsInterested(): Flow<List<Coin>>

    fun getCoins(sort: Sort): Flow<PagingData<Coin>>

    fun getCoinInfo(id: String): Flow<Coin?>

    suspend fun addInterest(coin: Coin)

    suspend fun deleteInterest(coin: Coin)
}