package com.hong7.coinnews.data.repository.impl

import android.util.Log
import com.hong7.coinnews.data.extensions.asResponseResourceFlow
import com.hong7.coinnews.data.mapper.toDomain
import com.hong7.coinnews.data.repository.CoinRepositoy
import com.hong7.coinnews.database.dao.CoinDao
import com.hong7.coinnews.model.Coin
import com.hong7.coinnews.model.CoinPrice
import com.hong7.coinnews.model.exception.ResponseResource
import com.hong7.coinnews.network.retrofit.UpbitService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CoinRepositoryImpl @Inject constructor(
    val upbitService: UpbitService,
    val coinDao: CoinDao,
) : CoinRepositoy {


//    override suspend fun isCoinListEmpty(): Boolean {
//        return coinDao.isEmpty()
//    }


    //    override fun getCoinListByQuery(query: String): Flow<List<Coin>> {
//        return coinDao.getCoinsByName(query)
//            .map { it.map { it.toDomain() } }
//    }
    override fun getCoins(): Flow<List<Coin>> {
        return coinDao.getCoins().map { it.map { it.toDomain() } }
    }

    override fun getCoinPrice(markets: List<String>): Flow<ResponseResource<List<CoinPrice>>> =
        flow {
            val joinedCoinIds = markets.joinToString(separator = ",")
            val coins = upbitService.fetchCoinPrice(joinedCoinIds)
                .map { it.toDomain() }
            emit(coins)
        }.asResponseResourceFlow()
}