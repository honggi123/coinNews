package com.hong7.coinnews.data.repository.impl

import com.hong7.coinnews.data.mapper.toDomain
import com.hong7.coinnews.data.mapper.toWatchListEntity
import com.hong7.coinnews.data.repository.WatchListRepository
import com.hong7.coinnews.database.dao.WatchListDao
import com.hong7.coinnews.model.Coin
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class WatchListRepositoryImpl @Inject constructor(
    private val watchListDao: WatchListDao
) : WatchListRepository {

    override fun getWatchList(): Flow<List<Coin>> {
        return watchListDao.getWatchListCoin()
            .map { it.map { it.toDomain() } }
    }

    override suspend fun addWatchListCoin(coin: Coin) {
        return watchListDao.addWatchListCoin(coin.toWatchListEntity())
    }

    override suspend fun removeWatchListCoin(coin: Coin) {
        return watchListDao.removeWatchListCoin(coin.id)
    }
}