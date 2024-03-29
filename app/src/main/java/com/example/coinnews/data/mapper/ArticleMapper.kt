package com.example.coinnews.data.mapper

import com.example.coinnews.network.model.NetworkArticle
import com.example.coinnews.model.Article
import com.example.coinnews.model.ArticleMetaData
import com.example.coinnews.model.Coin
import com.example.coinnews.model.CoinFilter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun NetworkArticle.toDomain(filter: CoinFilter): Article {
    return Article(
        id = "",
        title = this.title.replaceHtmlTags(),
        url = this.url,
        description = this.description.replaceHtmlTags(),
        metaData = ArticleMetaData(
            parseAuthorFromUrl(this.originalUrl),
            Coin(id = "", name = filter.coinName, slug = "", symbol = filter.symbol),
            LocalDateTime.now() // todo
        )
    )
}

private fun parseAuthorFromUrl(url: String): String? {
    val authors = listOf(
        "news1" to "연합뉴스",
        "sisaweek" to "시사위크",
        "fortunekorea" to "포츈코리아",
        "coinreaders" to "코인리더스",
        "newstapa" to "뉴스타파",
        "hankyung" to "한경신문",
        "gukjenews" to "국제뉴스"
    )

    return authors.find { (key, _) -> url.contains(key) }?.second
}

private fun formatToDateTime(dateTimeString: String): LocalDateTime {
    val formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss Z")
    return LocalDateTime.parse(dateTimeString, formatter)
}

private fun String.replaceHtmlTags(): String {
    return this.replace(Regex("<.*?>"), "")
        .replace("&quot;", "\"")
        .replace("&amp;", "&")
        .replace("&lt;", "<")
        .replace("&gt;", ">")
}