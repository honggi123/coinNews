package com.hong7.coinnews.data.repository

import com.hong7.coinnews.model.Coin
import kotlinx.coroutines.flow.Flow

interface CoinRepository {

    fun getAllCoins(): Flow<List<Coin>>
}