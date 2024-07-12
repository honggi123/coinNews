package com.hong7.coinnews.data.repository.impl

import android.util.Log
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

    override fun getFilterStream(): Flow<Filter?> {
        val filter = userFilterDao.getRecentFilterStream()
            .map { filter ->
                if (filter != null) filter.toDomain()
                else null
            }

        return filter
    }

    override suspend fun getFilter(): Filter? {
        val filter = userFilterDao.getRecentFilter()
        val mappedFilter =
            if (filter != null) filter.toDomain()
            else null
        return mappedFilter
    }

    override suspend fun isFilterEmpty(): Boolean {
        return userFilterDao.isEmpty()
    }

    override suspend fun saveSelectedCoin(coin: Coin) {
        TODO("Not yet implemented")
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