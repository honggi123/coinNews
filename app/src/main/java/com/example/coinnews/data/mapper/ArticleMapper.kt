package com.example.coinnews.data.mapper

import com.example.coinnews.database.NewsEntity
import com.example.coinnews.network.model.NetworkArticle
import com.example.coinnews.model.Article
import com.example.coinnews.ui.utils.DateUtils
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

fun NetworkArticle.toDomain(): Article {
    return Article(
        id = URLEncoder.encode(this.originalUrl, StandardCharsets.UTF_8.toString()),
        title = this.title.replaceHtmlTags(),
        url = this.url,
        author = parseAuthorFromUrl(this.originalUrl),
        createdAt = DateUtils.timeStringToTimestamp(this.createdAt)
    )
}

fun Article.toEntity(): NewsEntity {
    return NewsEntity(
        newsId = this.id,
        title = this.title.replaceHtmlTags(),
        url = this.url,
        author = this.author,
        createdAt = DateUtils.timeStampToLocalDateTime(this.createdAt)
    )
}

fun NewsEntity.toDomain(): Article {
    return Article(
        id = this.newsId,
        title = this.title.replaceHtmlTags(),
        url = this.url,
        author = this.author,
        createdAt = DateUtils.localDateTimeToTimeStamp(this.createdAt)
    )
}

private fun parseAuthorFromUrl(url: String): String? {
    val authors = listOf(
        "news1" to "연합뉴스",
        "sisaweek" to "시사위크",
        "fortunekorea" to "포츈 코리아",
        "coinreaders" to "코인 리더스",
        "newstapa" to "뉴스 타파",
        "hankyung" to "한경신문",
        "gukjenews" to "국제뉴스",
        "newsmin" to "뉴스민",
        "etoday" to "이투데이"
    )

    return authors.find { (key, _) -> url.contains(key) }?.second
}


private fun String.replaceHtmlTags(): String {
    return this.replace(Regex("<.*?>"), "")
        .replace("&quot;", "\"")
        .replace("&amp;", "&")
        .replace("&lt;", "<")
        .replace("&gt;", ">")
}