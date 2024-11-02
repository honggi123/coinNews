package com.hong7.coinnews.data.repository.impl

import com.hong7.coinnews.data.mapper.toDomain
import com.hong7.coinnews.data.repository.CoinRepositoy
import com.hong7.coinnews.database.dao.CoinDao
import com.hong7.coinnews.model.Coin
import com.hong7.coinnews.network.retrofit.CoinMarketCapService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CoinRepositoryImpl @Inject constructor(
    val coinMarketCapService: CoinMarketCapService,
    val coinDao: CoinDao
) : CoinRepositoy {
    override suspend fun isCoinListEmpty(): Boolean {
        return coinDao.isEmpty()
    }

    override fun getCoinListByQuery(query: String): Flow<List<Coin>> {
        return coinDao.getCoinsByName(query)
            .map { it.map { it.toDomain() } }
    }
}