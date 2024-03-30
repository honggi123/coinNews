package com.example.coinnews.data.repository

import androidx.paging.PagingData
import com.example.coinnews.model.Article
import com.example.coinnews.model.Coin
import com.example.coinnews.model.CoinFilter
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    fun getArticles(
        filter: CoinFilter?
    ): Flow<PagingData<Article>>
}