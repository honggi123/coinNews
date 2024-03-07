package com.example.coinnews.data.repository

import androidx.paging.PagingData
import com.example.coinnews.data.network.model.NetworkCoin
import com.example.coinnews.model.Article
import com.example.coinnews.model.Coin
import com.example.coinnews.model.CoinSortOption
import com.example.coinnews.model.Sort
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    fun getArticles(): Flow<PagingData<Article>>

    fun getCoins(
        option: CoinSortOption,
        sort: Sort
    ): Flow<PagingData<Coin>>
}