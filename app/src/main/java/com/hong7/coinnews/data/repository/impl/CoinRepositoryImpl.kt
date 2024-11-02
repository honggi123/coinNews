package com.hong7.coinnews.data.repository.impl

import android.util.Log
import com.hong7.coinnews.data.extensions.asResponseResourceFlow
import com.hong7.coinnews.data.mapper.toDomain
import com.hong7.coinnews.data.mapper.toWatchListEntity
import com.hong7.coinnews.data.repository.CoinRepositoy
import com.hong7.coinnews.database.dao.CoinDao
import com.hong7.coinnews.database.dao.WatchListDao
import com.hong7.coinnews.model.Coin
import com.hong7.coinnews.model.CoinQuote
import com.hong7.coinnews.model.News
import com.hong7.coinnews.model.exception.ResponseResource
import com.hong7.coinnews.network.retrofit.CoinMarketCapService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CoinRepositoryImpl @Inject constructor(
    val coinMarketCapService: CoinMarketCapService,
    val coinDao: CoinDao,
) : CoinRepositoy {
    override suspend fun isCoinListEmpty(): Boolean {
        return coinDao.isEmpty()
    }

    override fun getCoinQuote(coinIds: List<String>): Flow<ResponseResource<Map<String, CoinQuote>>> =
        flow {
            val joinedCoinIds = coinIds.joinToString(separator = ",")
            val coinQuotes = coinMarketCapService.fetchCoinQuote(joinedCoinIds)
                .data.mapValues {
                    it.value.toDomain()
                }
            emit(coinQuotes)
        }.asResponseResourceFlow()


    override fun getCoinListByQuery(query: String): Flow<List<Coin>> {
        return coinDao.getCoinsByName(query)
            .map { it.map { it.toDomain() } }
    }

}