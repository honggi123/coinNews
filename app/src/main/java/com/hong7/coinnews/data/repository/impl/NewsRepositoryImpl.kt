package com.hong7.coinnews.data.repository.impl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.hong7.coinnews.data.extensions.asResponseResourceFlow
import com.hong7.coinnews.data.mapper.toDomain
import com.hong7.coinnews.data.mapper.toScrapEntity
import com.hong7.coinnews.data.paging.NewsPagingSource
import com.hong7.coinnews.data.repository.NewsRepository
import com.hong7.coinnews.database.InterestedNewsDao
import com.hong7.coinnews.model.News
import com.hong7.coinnews.model.Coin
import com.hong7.coinnews.model.exception.ResponseResource
import com.hong7.coinnews.network.retrofit.NaverService
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
    private val interestedNewsDao: InterestedNewsDao,
    private val naverService: NaverService
) : NewsRepository {

    override fun getNews(query: String): Flow<PagingData<News>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = NETWORK_PAGE_SIZE),
            pagingSourceFactory = { NewsPagingSource(naverService, query) }
        ).flow
    }

    override fun getRecentNewsByQuery(query: String): Flow<ResponseResource<List<News>>> = flow {
        val news = fetchNaverNews(query)
        emit(news)
    }.asResponseResourceFlow()

    override fun getScrapedNewsList(): Flow<List<News>> {
        return interestedNewsDao.fetchAllNews()
            .map { it.map { it.toDomain() } }
    }

    // TODO : refactor
    private suspend fun fetchNaverNews(query: String): List<News> = withContext(Dispatchers.IO) {
        naverService.fetchNews(query = query, start = 1, pageSize = 30).items
            .map { it.toDomain() }
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 20
    }


//    override fun getRecentNewsByCoin(coin: Coin): Flow<ResponseResource<List<News>>> = flow {
//        val news = coroutineScope {
//            val query = "\"${coin.name}\""
//            val naverNewsDeffered = async { fetchNaverNews(query) }
//            naverNewsDeffered.await()
//        }
//        news.sortedByDescending { it.createdAt }
//        emit(news)
//    }.asResponseResourceFlow()

//    override fun isNewsScraped(newsId: String): Flow<Boolean> {
//        return interestedNewsDao.isInterested(newsId)
//    }
//
//    override suspend fun addNewsScraped(news: News) {
//        interestedNewsDao.insert(news.toScrapEntity())
//    }
//
//    override suspend fun deleteNewsScraped(news: News) {
//        interestedNewsDao.delete(news.id)
//    }

}