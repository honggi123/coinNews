package com.hong7.coinnews.data.repository

import com.hong7.coinnews.model.Coin
import kotlinx.coroutines.flow.Flow

interface CoinRepositoy {

    suspend fun isCoinListEmpty(): Boolean

    fun getCoinListByQuery(query: String): Flow<List<Coin>>
}