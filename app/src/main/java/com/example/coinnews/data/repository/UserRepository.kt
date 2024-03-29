package com.example.coinnews.data.repository

import com.example.coinnews.model.Article
import com.example.coinnews.model.CoinFilter
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun isEmpty(): Boolean

    fun getAllFilters(): Flow<List<CoinFilter>>

    fun getFilters(): Flow<List<CoinFilter>>

    suspend fun updateFilterSelect(filters: List<CoinFilter>)
}