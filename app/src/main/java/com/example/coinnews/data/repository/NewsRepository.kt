package com.example.coinnews.data.repository

import androidx.paging.PagingData
import com.example.coinnews.model.Article
import com.example.coinnews.model.Coin
import com.example.coinnews.model.CoinFilter
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    fun isInterested(id: String): Flow<Boolean>

    fun getArticles(
        filter: CoinFilter
    ): Flow<PagingData<Article>>

    suspend fun addInterest(article: Article)

    suspend fun deleteInterest(article: Article)
}