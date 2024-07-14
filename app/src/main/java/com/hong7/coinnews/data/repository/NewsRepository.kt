package com.hong7.coinnews.data.repository

import com.hong7.coinnews.model.News
import com.hong7.coinnews.model.Coin
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    suspend fun addNewsScraped(news: News)

    suspend fun deleteNewsScraped(news: News)

    fun isNewsScraped(newsId: String): Flow<Boolean>

    fun getRecentNewsByQuery(query: String): Flow<List<News>>

    fun getRecentNewsByCoin(coin: Coin): Flow<List<News>>

    fun getScrapedNewsList(): Flow<List<News>>
}