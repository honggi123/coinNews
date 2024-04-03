package com.example.coinnews.data.repository

import com.example.coinnews.model.Article
import com.example.coinnews.model.CoinFilter
import com.example.coinnews.model.CountryScope
import com.example.coinnews.model.Filter
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun getAllNews(): Flow<List<Article>>

    fun isNewsInterested(id: String): Flow<Boolean>

    suspend fun addNewsInterest(article: Article)

    suspend fun deleteNewsInterest(article: Article)

    fun getAllFilters(): Flow<Filter>

    suspend fun isFilterEmpty(): Boolean

    suspend fun updateFilter(filter: Filter)
}