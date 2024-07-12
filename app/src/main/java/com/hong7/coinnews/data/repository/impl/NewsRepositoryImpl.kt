package com.hong7.coinnews.data.repository.impl

import com.hong7.coinnews.data.mapper.toDomain
import com.hong7.coinnews.data.mapper.toEntity
import com.hong7.coinnews.data.mapper.toScrapEntity
import com.hong7.coinnews.data.repository.NewsRepository
import com.hong7.coinnews.data.util.ParsingManager
import com.hong7.coinnews.database.InterestedNewsDao
import com.hong7.coinnews.database.NewsDao
import com.hong7.coinnews.model.Article
import com.hong7.coinnews.model.Coin
import com.hong7.coinnews.network.okhttp.retrofit.NaverService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepositoryImpl @Inject constructor(
    private val newsDao: NewsDao,
    private val interestedNewsDao: InterestedNewsDao,
    private val naverService: NaverService
) : NewsRepository {

    override fun getRecentNewsByQuery(query: String): Flow<List<Article>> = flow {
        val news = ParsingManager.parseGoogleNews(query)
        newsDao.deleteAllNews()
        newsDao.insertAll(news.map { it.toEntity() })
        news.sortedByDescending { it.createdAt }
        emit(news)
    }

    override fun getRecentNewsByCoin(coin: Coin): Flow<List<Article>> = flow {
        val news = coroutineScope {
            val query = coin.name
            val naverNewsDeffered = async { getNaverNews(query) }
            val googleNewsDeffered = async { ParsingManager.parseGoogleNews(coin.name) }
            naverNewsDeffered.await() + googleNewsDeffered.await()
        }

        news.sortedByDescending { it.createdAt }
        emit(news)
    }

    override fun getScrapedNews(): Flow<List<Article>> {
        return interestedNewsDao.getAllNews()
            .map { it.map { it.toDomain() } }
    }

    override fun isNewsScraped(newsId: String): Flow<Boolean> {
        return interestedNewsDao.isInterested(newsId)
    }

    override suspend fun addNewsScraped(article: Article) {
        interestedNewsDao.insert(article.toScrapEntity())
    }

    override suspend fun deleteNewsScraped(article: Article) {
        interestedNewsDao.delete(article.id)
    }

    private suspend fun getNaverNews(query: String): List<Article> = withContext(Dispatchers.IO) {
        naverService.getArticles(query = query, page = 1, pageSize = 10).items
            .map { it.toDomain() }
    }

//    override suspend fun getSavedRecentNews(): List<Article> =
//        withContext(Dispatchers.IO) {
//            newsDao.getAllNews()
//                .map { it.toDomain() }
//        }
}