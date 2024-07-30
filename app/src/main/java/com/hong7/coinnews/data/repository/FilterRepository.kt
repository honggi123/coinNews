package com.hong7.coinnews.data.repository

import com.hong7.coinnews.model.Coin
import com.hong7.coinnews.model.Filter
import com.hong7.coinnews.model.exception.ResponseResource
import kotlinx.coroutines.flow.Flow

interface FilterRepository {

    fun getFilter(): Flow<ResponseResource<Filter>>

    fun getUserFilter(): Flow<Filter?>

    suspend fun isUserFilterEmpty(): Boolean

    suspend fun setMyCoins(selectedCoins: List<Coin>)
}