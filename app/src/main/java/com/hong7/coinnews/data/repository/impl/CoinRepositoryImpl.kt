package com.hong7.coinnews.data.repository.impl

import android.util.Log
import com.hong7.coinnews.data.mapper.toDomain
import com.hong7.coinnews.data.repository.CoinRepository
import com.hong7.coinnews.model.Coin
import com.hong7.coinnews.network.firebase.CoinDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CoinRepositoryImpl @Inject constructor(
    private val dataSource: CoinDataSource
) : CoinRepository {

    override fun getAllCoins(): Flow<List<Coin>> = flow {
        val list = dataSource.getAllCoins().map { it.toDomain() }
        emit(list)
    }
}