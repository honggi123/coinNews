package com.example.coinnews.data.repository.impl

import android.util.Log
import com.example.coinnews.data.mapper.toDomain
import com.example.coinnews.data.repository.NewsRepository
import com.example.coinnews.data.repository.UserRepository
import com.example.coinnews.database.CoinFilterDao
import com.example.coinnews.database.CoinFilterEntity
import com.example.coinnews.database.NewsDao
import com.example.coinnews.database.NewsEntity
import com.example.coinnews.model.Article
import com.example.coinnews.model.CoinFilter
import com.example.coinnews.network.retrofit.ArticleService
import com.example.coinnews.ui.utils.DateUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class UserRepositoryImpl @Inject constructor(
    val coinFilterDao: CoinFilterDao,
    val newsDao: NewsDao
) : UserRepository {

    override suspend fun isFilterEmpty(): Boolean {
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
            coinFilterDao.updateFilterSelect(it.id.toString(), it.isSelected)
        }
    }

    override fun getAllNews(): Flow<List<Article>> {
        return newsDao.getAllNews().map { it.map { it.toDomain() } }
    }

    override fun isNewsInterested(id: String): Flow<Boolean> {
        return newsDao.isInterested(id)
    }

    override suspend fun addNewsInterest(article: Article) {
        newsDao.insert(
            NewsEntity(
                newsId = article.id,
                title = article.title,
//                description = article.description,
                url = article.url,
                author = article.author,
                createdAt = DateUtils.timeStampToLocalDateTime(article.createdAt)
            )
        )
    }

    override suspend fun deleteNewsInterest(article: Article) {
        newsDao.delete(article.id)
    }
}