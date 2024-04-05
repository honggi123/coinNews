package com.hong7.coinnews.data.repository

import com.hong7.coinnews.model.Article
import com.hong7.coinnews.model.Filter
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun getAllNews(): Flow<List<Article>>

    fun isNewsInterested(id: String): Flow<Boolean>

    suspend fun addNewsInterest(article: Article)

    suspend fun deleteNewsInterest(article: Article)

    fun getAllFilters(): Flow<Filter?>

    suspend fun isFilterEmpty(): Boolean

    suspend fun updateFilter(filter: Filter)
}