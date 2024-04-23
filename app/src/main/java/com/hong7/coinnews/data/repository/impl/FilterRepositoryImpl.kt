package com.hong7.coinnews.data.repository.impl

import com.hong7.coinnews.data.mapper.toDomain
import com.hong7.coinnews.data.mapper.toEntity
import com.hong7.coinnews.data.repository.CoinRepository
import com.hong7.coinnews.data.repository.FilterRepository
import com.hong7.coinnews.database.CoinEntity
import com.hong7.coinnews.database.FilterEntity
import com.hong7.coinnews.database.UserFilterDao
import com.hong7.coinnews.model.Coin
import com.hong7.coinnews.model.Filter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FilterRepositoryImpl @Inject constructor(
    private val userFilterDao: UserFilterDao
) : FilterRepository {

    override fun getFilter(): Flow<Filter?> = flow {
        val filter = userFilterDao.getRecentFilter()
        val mappedFilter =
            if (filter != null) filter.toDomain()
            else null
        emit(mappedFilter)
    }

    override suspend fun isFilterEmpty(): Boolean {
        return userFilterDao.isEmpty()
    }

    override suspend fun setCoins(coins: List<Coin>) {
        val filter = userFilterDao.getRecentFilter()
        if (filter != null) {
            val coinEntities = coins.map { it.toEntity() }
            val newFilter = filter.copy(coins = coinEntities)
            userFilterDao.deleteFilter()
            userFilterDao.insert(newFilter)
        }
    }
}