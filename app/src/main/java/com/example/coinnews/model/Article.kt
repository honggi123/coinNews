package com.example.coinnews.model

import java.time.LocalDateTime

data class Article(
    val id: String,
    val title: String,
    val description: String,
    val url: String,
    val metaData: ArticleMetaData? = null
)

data class ArticleMetaData(
    val author: String? = null,
    val coin: Coin? = null,
    val createdAt: LocalDateTime? = null,
)

data class ArticleWithInterest(
    val article: Article,
    val isInterested: Boolean
)

