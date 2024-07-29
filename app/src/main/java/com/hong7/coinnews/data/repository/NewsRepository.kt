package com.hong7.coinnews.data.repository

import com.hong7.coinnews.model.News
import com.hong7.coinnews.model.Coin
import com.hong7.coinnews.model.exception.ResponseResource
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    suspend fun addNewsScraped(news: News)

    suspend fun deleteNewsScraped(news: News)

    fun isNewsScraped(newsId: String): Flow<Boolean>

    fun getRecentNewsByQuery(query: String): Flow<ResponseResource<List<News>>>

    fun getRecentNewsByCoin(coin: Coin): Flow<ResponseResource<List<News>>>

    fun getScrapedNewsList(): Flow<List<News>>
}