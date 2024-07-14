package com.hong7.coinnews.data.util

import com.hong7.coinnews.model.News
import com.hong7.coinnews.network.okhttp.RequestOriginalUrl
import com.hong7.coinnews.utils.DateUtils
import com.hong7.coinnews.utils.NumberUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup

object ParsingManager {

    suspend fun parseGoogleNews(query: String) = withContext(Dispatchers.IO) {
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
                News(
                    id = NumberUtils.getHashValue(originalUrl),
                    title = title,
                    url = originalUrl,
                    author = author,
                    createdAt = DateUtils.formatDateTimeWithUtcOffset(createdAt)
                )
            }
        }.awaitAll().filterNotNull()
        list
    }
}