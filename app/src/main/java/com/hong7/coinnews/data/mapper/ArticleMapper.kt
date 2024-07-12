package com.hong7.coinnews.data.mapper

import com.hong7.coinnews.database.NewsEntity
import com.hong7.coinnews.database.ScrapNewsEntity
import com.hong7.coinnews.network.model.NetworkArticle
import com.hong7.coinnews.model.Article
import com.hong7.coinnews.utils.DateUtils
import com.hong7.coinnews.utils.NumberUtils.getHashValue
import java.time.LocalDateTime
import java.util.Locale

fun NetworkArticle.toDomain(): Article {
    val article = Article(
        id = getHashValue(this.originalUrl),
        title = this.title.replaceHtmlTags(),
        url = this.url,
        description = this.description.replaceHtmlTags(),
        author = parseDomain(this.originalUrl),
        createdAt = DateUtils.formatDateTimeWithTimeZoneName(this.createdAt)
    )
    return article
}

fun NewsEntity.toDomain(): Article {
    return Article(
        id = this.newsId,
        title = this.title.replaceHtmlTags(),
        url = this.url,
        description = this.description,
        author = this.author,
        createdAt = this.createdAt
    )
}

fun ScrapNewsEntity.toDomain(): Article {
    return Article(
        id = this.newsId,
        title = this.title.replaceHtmlTags(),
        url = this.url,
        author = this.authorName,
        createdAt = this.createdAt
    )
}

fun Article.toEntity(): NewsEntity {
    return NewsEntity(
        newsId = this.id,
        title = this.title,
        description = this.description,
        url = this.url,
        author = this.author,
        createdAt = this.createdAt ?: LocalDateTime.now()
    )
}

fun Article.toScrapEntity(): ScrapNewsEntity {
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

