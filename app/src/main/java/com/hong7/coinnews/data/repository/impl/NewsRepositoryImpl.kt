package com.hong7.coinnews.data.repository.impl

import com.hong7.coinnews.data.mapper.toDomain
import com.hong7.coinnews.data.mapper.toEntity
import com.hong7.coinnews.data.mapper.toScrapEntity
import com.hong7.coinnews.data.repository.NewsRepository
import com.hong7.coinnews.database.InterestedNewsDao
import com.hong7.coinnews.database.NewsDao
import com.hong7.coinnews.model.Article
import com.hong7.coinnews.model.Coin
import com.hong7.coinnews.network.okhttp.RequestOriginalUrl
import com.hong7.coinnews.network.okhttp.retrofit.NaverService
import com.hong7.coinnews.utils.DateUtils
import com.hong7.coinnews.utils.NumberUtils.getHashValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepositoryImpl @Inject constructor(
    private val newsDao: NewsDao,
    private val interestedNewsDao: InterestedNewsDao,
    private val naverService: NaverService
) : NewsRepository {

    override fun getRecentNewsByQuery(query: String): Flow<List<Article>> = flow {
        withContext(Dispatchers.IO) {
            val result =
                Jsoup.connect("https://news.google.com/rss/search?q=${query}&hl=ko&gl=KR&ceid=KR%3Ako")
                    .get()
            val urlList = result.select("item").take(10)

            val list = urlList.mapNotNull {
                async {
                    val author = it.selectFirst("source")?.text()
                    val title = it.selectFirst("title")?.text()?.replace(" - ${author}", "")
                    val url = it.selectFirst("link")?.text()
                    val createdAt = it.selectFirst("pubDate")?.text()

                    if (author == null || title == null || url == null || createdAt == null) {
                        return@async null
                    }

                    val originalUrl = RequestOriginalUrl().invoke(url)
                    Article(
                        id = getHashValue(originalUrl),
                        title = title,
                        url = originalUrl,
                        author = author,
                        createdAt = DateUtils.formatDateTimeWithUtcOffset(createdAt)
                    )
                }
            }.awaitAll().filterNotNull()
            newsDao.deleteAllNews()
            newsDao.insertAll(list.map { it.toEntity() })
            list.sortedByDescending { it.createdAt }
        }
    }

    override fun getRecentNewsByCoin(coin: Coin): Flow<List<Article>> = flow {
        val news = coroutineScope {
            val query = coin.name
            val naverNewsDeffered = async { getNaverNews(query) }
            val googleNewsDeffered = async { getGoogleNews(coin.name) }
            naverNewsDeffered.await() + googleNewsDeffered.await()
        }

        news.sortedByDescending { it.createdAt }
        emit(news)
    }

    private suspend fun getGoogleNews(query: String): List<Article> =
        withContext(Dispatchers.IO) {
            val result =
                Jsoup.connect("https://news.google.com/rss/search?q=${query}&hl=ko&gl=KR&ceid=KR%3Ako")
                    .get()
            val urlList = result.select("item").take(10)

            val list = urlList.mapNotNull {
                async {
                    val author = it.selectFirst("source")?.text()
                    val title = it.selectFirst("title")?.text()?.replace(" - ${author}", "")
                    val url = it.selectFirst("link")?.text()
                    val createdAt = it.selectFirst("pubDate")?.text()

                    if (author == null || title == null || url == null || createdAt == null) {
                        return@async null
                    }

                    val originalUrl = RequestOriginalUrl().invoke(url)
                    Article(
                        id = getHashValue(originalUrl),
                        title = title,
                        url = originalUrl,
                        author = author,
                        createdAt = DateUtils.formatDateTimeWithUtcOffset(createdAt)
                    )
                }
            }.awaitAll().filterNotNull()
            list
        }

    private suspend fun getNaverNews(query: String): List<Article> = withContext(Dispatchers.IO) {
        naverService.getArticles(query = query, page = 1, pageSize = 10)
            .items
            .map { it.toDomain() }
    }

    override fun getScrapedNews(): Flow<List<Article>> {
        return interestedNewsDao.getAllNews()
            .map { it.map { it.toDomain() } }
    }

    override fun isNewsScraped(id: String): Flow<Boolean> {
        return interestedNewsDao.isInterested(id)
    }

    override suspend fun addNewsScraped(article: Article) {
        interestedNewsDao.insert(article.toScrapEntity())
    }

    override suspend fun deleteNewsScraped(article: Article) {
        interestedNewsDao.delete(article.id)
    }

//    override suspend fun getSavedRecentNews(): List<Article> =
//        withContext(Dispatchers.IO) {
//            newsDao.getAllNews()
//                .map { it.toDomain() }
//        }
}