package com.example.coinnews.data.mapper

import com.example.coinnews.data.network.model.NetworkArticle
import com.example.coinnews.model.Article
import com.example.coinnews.model.ArticleMetaData
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun NetworkArticle.toDomain(): Article {
    return Article(
        id = "",
        title = this.title,
        url = this.url,
        description = this.description,
        metaData = ArticleMetaData(
            parseAuthorFromUrl(this.originalUrl),
            parseDateTime(this.createdAt)
        )
    )
}

private fun parseAuthorFromUrl(url: String): String? {
    val authors = listOf(
        "news1" to "연합뉴스",
        "sisaweek" to "시사위크",
        "fortunekorea" to "포츈코리아",
        "coinreaders" to "코인리더스"
    )

    return authors.find { (key, _) -> url.contains(key) }?.second
}

private fun parseDateTime(dateTimeString: String): LocalDateTime {
    val formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss Z")
    return LocalDateTime.parse(dateTimeString, formatter)
}