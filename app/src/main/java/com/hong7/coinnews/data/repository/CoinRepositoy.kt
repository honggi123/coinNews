package com.hong7.coinnews.data.repository

import com.hong7.coinnews.model.Coin
import com.hong7.coinnews.model.CoinPrice
import com.hong7.coinnews.model.exception.ResponseResource
import kotlinx.coroutines.flow.Flow

interface CoinRepositoy {

    fun getCoins(): Flow<List<Coin>>

    fun getCoinPrice(markets: List<String>): Flow<ResponseResource<List<CoinPrice>>>
}