package com.hong7.coinnews.data.repository

import com.hong7.coinnews.model.Coin
import com.hong7.coinnews.model.Filter
import kotlinx.coroutines.flow.Flow

interface FilterRepository {

    fun getFilter(): Flow<Filter?>

    suspend fun isFilterEmpty(): Boolean

    suspend fun saveSelectedCoin(coin: Coin)

    suspend fun setMyCoins(selectedCoins: List<Coin>)
}