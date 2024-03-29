package com.example.coinnews.data.repository.impl

import com.example.coinnews.data.repository.NewsRepository
import com.example.coinnews.data.repository.UserRepository
import com.example.coinnews.database.CoinFilterDao
import com.example.coinnews.database.CoinFilterEntity
import com.example.coinnews.model.CoinFilter
import com.example.coinnews.network.retrofit.ArticleService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class UserRepositoryImpl @Inject constructor(
    val coinFilterDao: CoinFilterDao
) : UserRepository {

    override suspend fun isEmpty(): Boolean {
        return coinFilterDao.isEmpty()
    }

    override fun getAllFilters(): Flow<List<CoinFilter>> {
        return coinFilterDao.getAllFilters()
            .map {
                it.map {
                    CoinFilter(
                        it.id.toString(),
                        it.coinName,
                        it.symbol,
                        it.isSelected
                    )
                }
            }
    }

    override fun getFilters(): Flow<List<CoinFilter>> {
        return coinFilterDao.getSelctedFilters()
            .map {
                it.map {
                    CoinFilter(
                        it.id.toString(),
                        it.coinName,
                        it.symbol,
                        it.isSelected
                    )
                }
            }
    }

    override suspend fun updateFilterSelect(filters: List<CoinFilter>) {
        filters.forEach {
            coinFilterDao.updateFilterSelect(it.id.toString(), it.isSelected)   // todo
        }
    }
}