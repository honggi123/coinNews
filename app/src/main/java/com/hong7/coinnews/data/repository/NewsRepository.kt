package com.hong7.coinnews.data.repository

import com.hong7.coinnews.model.Article
import com.hong7.coinnews.model.NetworkResult
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    suspend fun getGoogleNews(query: String): NetworkResult<List<Article>>

    suspend fun getNaverNews(query: String): NetworkResult<List<Article>>

    fun getScrapedNews(): Flow<List<Article>>

    fun isNewsScraped(id: String): Flow<Boolean>

    suspend fun addNewsScraped(article: Article)

    suspend fun deleteNewsScraped(article: Article)
}