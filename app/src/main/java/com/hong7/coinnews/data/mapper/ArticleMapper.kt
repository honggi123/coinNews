package com.hong7.coinnews.data.mapper

import android.util.Log
import com.google.firebase.perf.FirebasePerformance
import com.google.firebase.perf.metrics.Trace
import com.hong7.coinnews.database.NewsEntity
import com.hong7.coinnews.network.model.NetworkArticle
import com.hong7.coinnews.model.Article
import com.hong7.coinnews.network.model.NetworkGlobalNews
import com.hong7.coinnews.ui.utils.DateUtils
import java.security.MessageDigest
import java.util.Locale

fun NetworkArticle.toDomain(): Article {
    val article = Article(
        id = generateId(this.title + this.createdAt),
        title = this.title.replaceHtmlTags(),
        url = this.url,
        description = this.description.replaceHtmlTags(),
        author = parseDomain(this.originalUrl),
        createdAt = DateUtils.stringToDateTime(this.createdAt)
    )
    return article
}

fun NetworkGlobalNews.toDomain(): Article {
    return Article(
        id = generateId(this.title + this.createdAt),
        title = this.title,
        url = this.newsUrl,
        description = this.text,
        author = this.author,
        createdAt = DateUtils.stringToDateTime(this.createdAt)
    )
}

//fun Article.toEntity(): NewsEntity {
//    return NewsEntity(
//        newsId = this.id,
//        title = this.title.replaceHtmlTags(),
//        url = this.url,
//        author = this.author,
//        description = this.description,
//        createdAt = DateUtils.timeStampToLocalDateTime(this.createdAt)
//    )
//}

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

private fun generateId(value: String): String {
    val bytes = value.toByteArray()
    val digest = MessageDigest.getInstance("SHA-256").digest(bytes)
    return digest.fold("", { str, it -> str + "%02x".format(it) })
}