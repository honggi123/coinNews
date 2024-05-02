package com.hong7.coinnews.data.repository

import androidx.paging.PagingData
import com.hong7.coinnews.model.Article
import com.hong7.coinnews.model.Coin
import com.hong7.coinnews.model.NetworkResult
import kotlinx.coroutines.flow.Flow

interface CoinRepository {

    suspend fun getAllCoins(): NetworkResult<List<Coin>>
}