package com.hong7.coinnews.data.repository.impl

import com.hong7.coinnews.data.mapper.toDomain
import com.hong7.coinnews.data.mapper.toEntity
import com.hong7.coinnews.data.repository.CoinRepository
import com.hong7.coinnews.model.Coin
import com.hong7.coinnews.model.NetworkResult
import com.hong7.coinnews.model.mapNetworkResult
import com.hong7.coinnews.network.firebase.CoinDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CoinRepositoryImpl @Inject constructor(
    private val dataSource: CoinDataSource
) : CoinRepository {

    override suspend fun getAllCoins(): NetworkResult<List<Coin>>{
        return dataSource.getAllCoins().mapNetworkResult {
            it.toList().map { it.toDomain() }
        }
    }
}