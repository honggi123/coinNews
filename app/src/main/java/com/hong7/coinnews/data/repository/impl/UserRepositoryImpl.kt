package com.hong7.coinnews.data.repository.impl

import com.hong7.coinnews.data.mapper.toDomain
import com.hong7.coinnews.data.mapper.toEntity
import com.hong7.coinnews.data.repository.UserRepository
import com.hong7.coinnews.database.FilterDao
import com.hong7.coinnews.database.NewsDao
import com.hong7.coinnews.database.NewsEntity
import com.hong7.coinnews.model.Article
import com.hong7.coinnews.model.Filter
import kotlinx.coroutines.flow.Flow
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

    override fun getAllFilters(): Flow<Filter?> {
        return filterDao.getAllFilters().map { it?.toDomain() }
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
                createdAt = article.createdAt ?: LocalDateTime.now()
            )
        )
    }

    override suspend fun deleteNewsInterest(article: Article) {
        newsDao.delete(article.id)
    }
}