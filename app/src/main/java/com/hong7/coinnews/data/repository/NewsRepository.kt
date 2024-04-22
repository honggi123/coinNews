package com.hong7.coinnews.data.repository

import androidx.paging.PagingData
import com.hong7.coinnews.model.Article
import com.hong7.coinnews.model.Coin
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    fun getArticles(
        coin: Coin
    ): Flow<PagingData<Article>>

    fun getGlobalArticles(
        coin: Coin
    ): Flow<PagingData<Article>>

    fun getScrapedNews(): Flow<List<Article>>

    fun isNewsScraped(id: String): Flow<Boolean>

    suspend fun addNewsScraped(article: Article)

    suspend fun deleteNewsScraped(article: Article)
}