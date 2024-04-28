package com.hong7.coinnews.data.repository.impl

import android.util.Log
import com.hong7.coinnews.data.mapper.toDomain
import com.hong7.coinnews.data.repository.NewsRepository
import com.hong7.coinnews.database.NewsEntity
import com.hong7.coinnews.database.UserNewsDao
import com.hong7.coinnews.model.Article
import com.hong7.coinnews.model.Coin
import com.hong7.coinnews.network.model.NetworkArticle
import com.hong7.coinnews.network.retrofit.NaverService
import com.hong7.coinnews.ui.utils.NumberUtils.getHashValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException
import java.net.URL
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Base64
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class NewsRepositoryImpl @Inject constructor(
    private val naverService: NaverService,
    private val userNewsDao: UserNewsDao
) : NewsRepository {

    private suspend fun getGoogleNews(query: String): List<Article> = withContext(Dispatchers.IO) {
        val list = mutableListOf<Article>()
        val result =
            Jsoup.connect("https://news.google.com/rss/search?q=${query}&hl=ko&gl=KR&ceid=KR%3Ako")
                .get()
        val urlList = result.select("item").slice(0..10)

        urlList.map {
            async {
                val title = it.selectFirst("title").text()
                val url = it.selectFirst("link").text()
                val author = it.selectFirst("source").text()
                val createdAt = it.selectFirst("pubDate").text()
                val originalUrl = fetchUrlFromGoogleNewsRss(url)

                val article = Article(
                    id = getHashValue(originalUrl),
                    title = title,
                    description = "",
                    url = originalUrl,
                    author = author,
                    createdAt = stringToDate(createdAt)
                )
                list.add(article)
            }
        }.awaitAll()
        list
    }

    fun fetchUrlFromGoogleNewsRss(rssUrl: String): String {
        val client = OkHttpClient()

        val request = Request.Builder()
            .url(rssUrl)
            .header("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36")
            .addHeader("Cookie", "CONSENT=YES+cb.20220419-08-p0.cs+FX+111")
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            val htmlContent = response.body?.string() ?: throw IOException("Empty response body")
            val soup: Document = Jsoup.parse(htmlContent)

            val link = soup.selectFirst("a")?.attr("href") ?: "No link found"

            return link
        }
    }

    private fun stringToDate(dateTimeString: String): LocalDateTime {
        val formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss z", Locale.US)
        return LocalDateTime.parse(dateTimeString, formatter)
    }

    private suspend fun getNaverNews(query: String): List<NetworkArticle> {
        val response = naverService.getArticles(
            query = query,
            page = 1,
            pageSize = 10
        )
        return response.items
    }

    override suspend fun getRecentArticles(coin: Coin): List<Article> {
        val searchWordsWithPlus = coin.relatedSearchWord.joinToString(separator = " | ")
        val query = "${coin.name} | ${searchWordsWithPlus}"
        val naverNewsList = getNaverNews(query).map { it.toDomain() }
        val googleNewsList = getGoogleNews(coin.name)

        val list = (naverNewsList + googleNewsList)
            .distinctBy { it.id }
            .sortedByDescending { it.createdAt }
            .take(11)

        return list
    }

    override fun getScrapedNews(): Flow<List<Article>> {
        return userNewsDao.getAllNews()
            .map { it.map { it.toDomain() } }
    }

    override fun isNewsScraped(id: String): Flow<Boolean> {
        return userNewsDao.isInterested(id)
    }

    override suspend fun addNewsScraped(article: Article) {
        userNewsDao.insert(
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

    override suspend fun deleteNewsScraped(article: Article) {
        userNewsDao.delete(article.id)
    }
}