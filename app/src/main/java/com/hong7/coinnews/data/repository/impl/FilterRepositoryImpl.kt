package com.hong7.coinnews.data.repository.impl

import android.util.Log
import com.hong7.coinnews.data.mapper.toDomain
import com.hong7.coinnews.data.mapper.toEntity
import com.hong7.coinnews.data.repository.FilterRepository
import com.hong7.coinnews.database.FilterEntity
import com.hong7.coinnews.database.UserFilterDao
import com.hong7.coinnews.model.Coin
import com.hong7.coinnews.model.Filter
import com.hong7.coinnews.network.firebase.CoinDataSource
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FilterRepositoryImpl @Inject constructor(
    private val dataSource: CoinDataSource,
    private val userFilterDao: UserFilterDao
) : FilterRepository {

    override fun getFilter(): Flow<Filter> = flow {
        val coins = dataSource.getAllCoins()
            .map { it.toDomain() }
        emit(Filter(coins = coins.toImmutableList()))
    }

    override fun getUserFilter(): Flow<Filter?> {
        val filter = userFilterDao.getRecentFilterStream()
            .map { filter -> filter?.let { it.toDomain() } }

        return filter
    }

    override suspend fun isUserFilterEmpty(): Boolean {
        return userFilterDao.isEmpty()
    }

    override suspend fun setMyCoins(selectedCoins: List<Coin>) {
        val filter = userFilterDao.getRecentFilter()
        val coinEntities = selectedCoins.map { it.toEntity() }

        val newFilter = if (filter != null) {
            filter.copy(coins = coinEntities)
        } else {
            FilterEntity(coins = coinEntities)
        }
        userFilterDao.deleteFilter()
        userFilterDao.insert(newFilter)
    }
}