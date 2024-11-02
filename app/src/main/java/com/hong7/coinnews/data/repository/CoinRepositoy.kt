package com.hong7.coinnews.data.repository

import com.hong7.coinnews.model.Coin
import com.hong7.coinnews.model.CoinQuote
import com.hong7.coinnews.model.exception.ResponseResource
import kotlinx.coroutines.flow.Flow

interface CoinRepositoy {

    suspend fun isCoinListEmpty(): Boolean

    fun getCoinQuote(coinIds: List<String>): Flow<ResponseResource<Map<String, CoinQuote>>>

    fun getCoinListByQuery(query: String): Flow<List<Coin>>
}