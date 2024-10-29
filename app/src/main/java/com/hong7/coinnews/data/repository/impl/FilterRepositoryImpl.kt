package com.hong7.coinnews.data.repository.impl

import com.hong7.coinnews.data.extensions.asResponseResourceFlow
import com.hong7.coinnews.data.mapper.toDomain
import com.hong7.coinnews.data.mapper.toEntity
import com.hong7.coinnews.data.repository.FilterRepository
import com.hong7.coinnews.database.entity.FilterEntity
import com.hong7.coinnews.database.dao.UserFilterDao
import com.hong7.coinnews.model.Coin
import com.hong7.coinnews.model.Filter
import com.hong7.coinnews.model.exception.ResponseResource
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

    override fun getFilter(): Flow<ResponseResource<Filter>> = flow {
        val coins = dataSource.fetchAllCoins()
            .map { it.toDomain() }
        emit(Filter(coins = coins.toImmutableList()))
    }.asResponseResourceFlow()

    override fun getUserFilter(): Flow<Filter?> {
        val filter = userFilterDao.getRecentFilterStream()
            .map { filter -> filter?.let { it.toDomain() } }

        return filter
    }

    override suspend fun isUserFilterEmpty(): Boolean {
        return userFilterDao.isEmpty()
    }

    override suspend fun setMyCoinsFilter(coins: List<Coin>) {
        val oldFilter = userFilterDao.getRecentFilter()
        val coinEntities = coins.map { it.toEntity() }

        val updatedCoins = if (oldFilter != null) {
            val savedList = oldFilter.coins.toMutableList()
             savedList + coinEntities
        } else {
            coinEntities
        }

        val newFilter = oldFilter?.copy(coins = updatedCoins) ?: FilterEntity(coins = coinEntities)
        userFilterDao.insert(newFilter)
    }
}

