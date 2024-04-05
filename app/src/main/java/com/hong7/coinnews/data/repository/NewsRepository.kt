package com.hong7.coinnews.data.repository

import androidx.paging.PagingData
import com.hong7.coinnews.model.Article
import com.hong7.coinnews.model.CoinFilter
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    fun getArticles(
        filter: CoinFilter?
    ): Flow<PagingData<Article>>

    fun getGlobalArticles(
        filter: CoinFilter
    ): Flow<PagingData<Article>>
}