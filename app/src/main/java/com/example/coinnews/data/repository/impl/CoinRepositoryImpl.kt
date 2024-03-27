package com.example.coinnews.data.repository.impl

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.coinnews.data.mapper.toDomain
import com.example.coinnews.data.mapper.toEntity
import com.example.coinnews.data.mapper.toNetwork
import com.example.coinnews.network.retrofit.ArticleService
import com.example.coinnews.network.retrofit.CoinService
import com.example.coinnews.data.paging.ArticlePagingSource
import com.example.coinnews.data.paging.CoinPagingSource
import com.example.coinnews.data.repository.CoinRepository
import com.example.coinnews.data.repository.NewsRepository
import com.example.coinnews.database.CoinInterestedDao
import com.example.coinnews.model.Article
import com.example.coinnews.model.Coin
import com.example.coinnews.model.Sort
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CoinRepositoryImpl @Inject constructor(
    private val coinService: CoinService,
    private val coinInterestedDao: CoinInterestedDao
) : CoinRepository {

//    override fun isInterested(coinId: String): Flow<Boolean> {
//        return coinInterestedDao.isInterested(coinId)
//    }
//
//    override fun getCoinsInterested(): Flow<List<Coin>> {
//        return coinInterestedDao.getCoinInterestedList()
//            .map { list ->
//                list.map { it.toDomain() }
//            }
//    }

//    override fun getCoins(
//        sort: Sort,
//    ): Flow<PagingData<Coin>> {
//        val sortOption = sort.option.toNetwork()
//        val ordering = sort.ordering.toNetwork()
//        return Pager(
//            config = PagingConfig(enablePlaceholders = false, pageSize = 10),
//            pagingSourceFactory = { CoinPagingSource(sortOption, ordering, coinService) }
//        ).flow.map { list ->
//            list.map { it.toDomain() }
//        }
//    }

    override fun getCoinInfo(
        coin: Coin,
    ): Flow<Coin?> = flow {
        val coinItems = coinService.getCoinInfo(symbol = coin.symbol).items
        val coin = coinItems.firstNotNullOf { it.value }.toDomain()
        emit(coin)
    }

//    override suspend fun addInterest(coin: Coin) {
//        val coinEntity = coin.toEntity()
//        coinInterestedDao.insertCoinInterested(coinEntity)
//    }
//
//    override suspend fun deleteInterest(coin: Coin) {
//        coinInterestedDao.deleteCoinInterested(coin.id)
//    }
}