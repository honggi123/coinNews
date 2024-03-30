package com.example.coinnews.data.repository

import com.example.coinnews.model.Article
import com.example.coinnews.model.CoinFilter
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun getAllNews(): Flow<List<Article>>

    fun isNewsInterested(id: String): Flow<Boolean>

    suspend fun addNewsInterest(article: Article)

    suspend fun deleteNewsInterest(article: Article)

    fun getAllFilters(): Flow<List<CoinFilter>>

    fun getFilters(): Flow<List<CoinFilter>>

    suspend fun isFilterEmpty(): Boolean

    suspend fun updateFilterSelect(filters: List<CoinFilter>)
}