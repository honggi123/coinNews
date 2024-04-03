package com.example.coinnews.data.mapper

import androidx.compose.ui.text.capitalize
import com.example.coinnews.database.NewsEntity
import com.example.coinnews.network.model.NetworkArticle
import com.example.coinnews.model.Article
import com.example.coinnews.network.model.NetworkGlobalNews
import com.example.coinnews.ui.utils.DateUtils
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.util.Locale

fun NetworkArticle.toDomain(): Article {
    return Article(
        id = generateSafeUrlId(this.originalUrl),
        title = this.title.replaceHtmlTags(),
        url = this.url,
        description = this.description.replaceHtmlTags(),
        author = parseDomain(this.originalUrl),
        createdAt = DateUtils.timeStringToTimestamp(this.createdAt)
    )
}

fun NetworkGlobalNews.toDomain(): Article {
    return Article(
        id = generateSafeUrlId(this.newsUrl),
        title = this.title,
        url = this.newsUrl,
        description = this.text,
        author = this.author,
        createdAt = DateUtils.timeStringToTimestamp(this.createdAt)
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
        createdAt = DateUtils.localDateTimeToTimeStamp(this.createdAt)
    )
}


private fun parseDomain(url: String): String? {
    val regex = Regex("(?:https?://)?(?:www\\.)?([a-zA-Z0-9-]+)\\.[a-zA-Z]{2,6}")
    val matchResult = regex.find(url)
    return matchResult?.groupValues?.get(1)?.replaceFirstChar(Char::uppercase)
}


private fun String.replaceHtmlTags(): String {
    return this.replace(Regex("<.*?>"), "")
        .replace("&quot;", "\"")
        .replace("&amp;", "&")
        .replace("&lt;", "<")
        .replace("&gt;", ">")
}

private fun generateSafeUrlId(url: String): String {
    val bytes = url.toByteArray()
    val digest = MessageDigest.getInstance("SHA-256").digest(bytes)
    return digest.fold("", { str, it -> str + "%02x".format(it) })
}