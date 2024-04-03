package com.example.coinnews.data.repository.impl

import android.util.Log
import com.example.coinnews.data.mapper.toDomain
import com.example.coinnews.data.mapper.toEntity
import com.example.coinnews.data.repository.UserRepository
import com.example.coinnews.database.FilterDao
import com.example.coinnews.database.NewsDao
import com.example.coinnews.database.NewsEntity
import com.example.coinnews.model.Article
import com.example.coinnews.model.CoinFilter
import com.example.coinnews.model.CountryScope
import com.example.coinnews.model.Filter
import com.example.coinnews.ui.utils.DateUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class UserRepositoryImpl @Inject constructor(
    val filterDao: FilterDao,
    val newsDao: NewsDao
) : UserRepository {

    override suspend fun isFilterEmpty(): Boolean {
        return filterDao.isEmpty()
    }

    override suspend fun updateFilter(filter: Filter) {
        filterDao.insert(filter.toEntity())
    }

    override fun getAllFilters(): Flow<Filter> {
        return filterDao.getAllFilters()
            .filterNotNull()
            .map { it.toDomain() }
    }

    override fun getAllNews(): Flow<List<Article>> {
        return newsDao.getAllNews()
            .map { it.map { it.toDomain() } }
    }

    override fun isNewsInterested(id: String): Flow<Boolean> {
        return newsDao.isInterested(id)
    }

    override suspend fun addNewsInterest(article: Article) {
        newsDao.insert(
            NewsEntity(
                newsId = article.id,
                title = article.title,
                description = article.description,
                url = article.url,
                author = article.author,
                createdAt = article.createdAt?.let { DateUtils.timeStampToLocalDateTime(it) } ?: LocalDateTime.now() // todo
            )
        )
    }

    override suspend fun deleteNewsInterest(article: Article) {
        newsDao.delete(article.id)
    }
}