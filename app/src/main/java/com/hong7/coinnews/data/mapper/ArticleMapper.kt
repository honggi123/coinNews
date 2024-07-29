package com.hong7.coinnews.data.mapper

import com.hong7.coinnews.database.entity.NewsEntity
import com.hong7.coinnews.database.entity.ScrapNewsEntity
import com.hong7.coinnews.network.model.NetworkNews
import com.hong7.coinnews.model.News
import com.hong7.coinnews.utils.DateUtils
import com.hong7.coinnews.utils.NumberUtils.getHashValue
import java.time.LocalDateTime
import java.util.Locale

fun NetworkNews.toDomain(): News {
    val news = News(
        id = getHashValue(this.title),
        title = this.title.replaceHtmlTags(),
        url = this.url,
        description = this.description.replaceHtmlTags(),
        author = parseDomain(this.originalUrl),
        createdAt = DateUtils.formatDateTimeWithTimeZoneName(this.createdAt)
    )
    return news
}

fun NewsEntity.toDomain(): News {
    return News(
        id = this.newsId,
        title = this.title.replaceHtmlTags(),
        url = this.url,
        description = this.description,
        author = this.author,
        createdAt = this.createdAt
    )
}

fun ScrapNewsEntity.toDomain(): News {
    return News(
        id = this.newsId,
        title = this.title.replaceHtmlTags(),
        url = this.url,
        author = this.authorName,
        createdAt = this.createdAt
    )
}

fun News.toEntity(): NewsEntity {
    return NewsEntity(
        newsId = this.id,
        title = this.title,
        description = this.description,
        url = this.url,
        author = this.author,
        createdAt = this.createdAt ?: LocalDateTime.now()
    )
}

fun News.toScrapEntity(): ScrapNewsEntity {
    return ScrapNewsEntity(
        newsId = this.id,
        title = this.title,
        url = this.url,
        authorName = this.author,
        createdAt = this.createdAt ?: LocalDateTime.now()
    )
}



private fun parseDomain(url: String): String? {
    val regex = Regex("(?:https?://)?(?:www\\.)?([a-zA-Z0-9-]+)\\.[a-zA-Z]{2,6}")
    val matchResult = regex.find(url)
    return matchResult?.groupValues?.get(1)?.uppercase(Locale.ROOT)
}


private fun String.replaceHtmlTags(): String {
    return this.replace(Regex("<.*?>"), "")
        .replace("&quot;", "\"")
        .replace("&amp;", "&")
        .replace("&lt;", "<")
        .replace("&gt;", ">")
}

