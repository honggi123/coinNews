package com.hong7.coinnews.data.repository

import com.hong7.coinnews.data.mapper.toEntity
import com.hong7.coinnews.model.Coin
import com.hong7.coinnews.model.Filter
import kotlinx.coroutines.flow.Flow

interface FilterRepository {

     fun getFilterStream(): Flow<Filter?>

     suspend fun getFilter(): Filter?

     suspend fun isFilterEmpty(): Boolean

     suspend fun setCoins(selectedCoins: List<Coin>)
}