package com.hong7.coinnews.data.repository.impl

import com.hong7.coinnews.data.extensions.asResponseResourceFlow
import com.hong7.coinnews.data.mapper.toDomain
import com.hong7.coinnews.data.repository.CoinRepositoy
import com.hong7.coinnews.database.dao.CoinDao
import com.hong7.coinnews.model.Coin
import com.hong7.coinnews.model.CoinPrice
import com.hong7.coinnews.model.exception.ResponseResource
import com.hong7.coinnews.network.retrofit.BithumbService
import com.hong7.coinnews.network.retrofit.UpbitService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CoinRepositoryImpl @Inject constructor(
    val bithumbService: BithumbService,
    val upbitService: UpbitService,
    val coinDao: CoinDao,
) : CoinRepositoy {
    override fun getBithumbCoins(): Flow<ResponseResource<List<Coin>>> {
        return flow {
            val coinListResponse = bithumbService.fetchCoinList()
            val joinedMarkets = coinListResponse.joinToString(",") { it.marketId }
            val coinMap = coinListResponse.associateBy { it.marketId }

            val coinPriceResponse = bithumbService.fetchCoinPrice(joinedMarkets)
            val coins = coinPriceResponse.map { coinPrice ->
                val coin = coinMap[coinPrice.market]

                Coin(
                    marketId = coin?.marketId.orEmpty(),
                    koreanName = coin?.koreanName.orEmpty(),
                    englishName = coin?.englishName.orEmpty(),
                    tradePrice = coinPrice.tradePrice,
                    changeRate = coinPrice.changeRate,
                    accTradePrice24h = coinPrice.accTradePrice24h
                )
            }
            emit(coins)
        }.asResponseResourceFlow()
    }

    override fun getCoins(): Flow<List<Coin>>
        = coinDao.getCoins().map { it.map { it.toDomain() } }


    override fun getCoinPrice(markets: List<String>): Flow<ResponseResource<List<CoinPrice>>> {
       return flow {
            val joinedCoinIds = markets.joinToString(separator = ",")
            val coins = upbitService.fetchCoinPrice(joinedCoinIds)
                .map { it.toDomain() }
            emit(coins)
        }.asResponseResourceFlow()
    }
}