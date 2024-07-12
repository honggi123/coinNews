package com.hong7.coinnews.data.repository

import com.hong7.coinnews.model.Article
import com.hong7.coinnews.model.Coin
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    suspend fun addNewsScraped(article: Article)

    suspend fun deleteNewsScraped(article: Article)

    fun isNewsScraped(newsId: String): Flow<Boolean>

    fun getRecentNewsByQuery(query: String): Flow<List<Article>>

    fun getRecentNewsByCoin(coin: Coin): Flow<List<Article>>

    fun getScrapedNews(): Flow<List<Article>>
}