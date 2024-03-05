package com.example.coinnews.model

import java.time.LocalDateTime

data class Article(
    val id: String,
    val title: String,
    val description: String,
    val url: String,
    val metaData: ArticleMetaData
)

data class ArticleMetaData(
    val author: String?,
    val createdAt: LocalDateTime,
)

