package com.example.coinnews.data.repository.impl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.coinnews.data.mapper.toDomain
import com.example.coinnews.data.mapper.toNetwork
import com.example.coinnews.data.network.retrofit.CoinService
import com.example.coinnews.data.network.retrofit.NewsService
import com.example.coinnews.data.paging.CoinPagingSource
import com.example.coinnews.data.paging.NewsPagingSource
import com.example.coinnews.data.repository.NewsRepository
import com.example.coinnews.model.Article
import com.example.coinnews.model.Asset
import com.example.coinnews.model.Coin
import com.example.coinnews.model.CoinSortOption
import com.example.coinnews.model.Sort
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private const val DEFAULT_CRYPTO_QUERY = "암호화폐"

@Singleton
class NewsRepositoryImpl @Inject constructor(
    private val newsService: NewsService,
    private val coinService: CoinService
) : NewsRepository {

    override fun getArticles(): Flow<PagingData<Article>> {
        return Pager(
            config = PagingConfig(
                enablePlaceholders = false, // todo
                pageSize = 5
            ),
            pagingSourceFactory = {
                NewsPagingSource(
                    service = newsService,
                    query = DEFAULT_CRYPTO_QUERY
                )
            }
        ).flow.map { it.map { it.toDomain() } }
    }

    override fun getCoins(
        option: CoinSortOption,
        sort: Sort,
    ): Flow<PagingData<Coin>> {
        return Pager(
            config = PagingConfig(
                enablePlaceholders = false,
                pageSize = 5
            ),
            pagingSourceFactory = {
                CoinPagingSource(
                    sortOption = option.toNetwork(),
                    sort = sort.toNetwork(),
                    service = coinService
                )
            }
        ).flow.map { it.map { it.toDomain() } }
    }
}