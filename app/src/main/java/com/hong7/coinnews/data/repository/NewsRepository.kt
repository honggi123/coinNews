package com.hong7.coinnews.data.repository

import androidx.paging.PagingData
import com.hong7.coinnews.model.Article
import com.hong7.coinnews.model.Coin
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    suspend fun getRecentArticles(
        coin: Coin
    ): List<Article>

    fun getScrapedNews(): Flow<List<Article>>

    fun isNewsScraped(id: String): Flow<Boolean>

    suspend fun addNewsScraped(article: Article)

    suspend fun deleteNewsScraped(article: Article)
}