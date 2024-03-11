package com.example.coinnews.data.repository.impl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.coinnews.data.mapper.toDomain
import com.example.coinnews.data.mapper.toNetwork
import com.example.coinnews.network.retrofit.ArticleService
import com.example.coinnews.network.retrofit.CoinService
import com.example.coinnews.data.paging.ArticlePagingSource
import com.example.coinnews.data.paging.CoinPagingSource
import com.example.coinnews.data.repository.CoinRepository
import com.example.coinnews.data.repository.NewsRepository
import com.example.coinnews.model.Article
import com.example.coinnews.model.Coin
import com.example.coinnews.model.Sort
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CoinRepositoryImpl @Inject constructor(
    private val coinService: CoinService
) : CoinRepository {

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

    override fun getCoinInfo(id: String): Flow<Coin?> = flow {
        val coinItems = coinService.getCoinInfo(id = id).items
        val coin = coinItems.get(id)?.toDomain()
        emit(coin)
        // todo try catch
    }
}