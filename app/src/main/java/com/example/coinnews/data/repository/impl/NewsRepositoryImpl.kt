package com.example.coinnews.data.repository.impl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.coinnews.data.mapper.toDomain
import com.example.coinnews.data.mapper.toNetwork
import com.example.coinnews.data.network.retrofit.CoinService
import com.example.coinnews.data.network.retrofit.ArticleService
import com.example.coinnews.data.paging.CoinPagingSource
import com.example.coinnews.data.paging.ArticlePagingSource
import com.example.coinnews.data.repository.NewsRepository
import com.example.coinnews.model.Article
import com.example.coinnews.model.Coin
import com.example.coinnews.model.Sort
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepositoryImpl @Inject constructor(
    private val articleService: ArticleService,
    private val coinService: CoinService
) : NewsRepository {

    override fun getArticles(): Flow<PagingData<Article>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = 10),
            pagingSourceFactory = { ArticlePagingSource(articleService, DEFAULT_CRYPTO_QUERY) }
        ).flow.map { it.map { it.toDomain() } }
    }

    override fun getCoins(
        sort: Sort,
    ): Flow<PagingData<Coin>> {
        val sortOption = sort.option.toNetwork()
        val ordering = sort.ordering.toNetwork()
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = 10),
            pagingSourceFactory = { CoinPagingSource(sortOption, ordering, coinService) }
        ).flow.map { it.map { it.toDomain() } }
    }

    companion object {
        private const val DEFAULT_CRYPTO_QUERY = "암호화폐"
    }
}