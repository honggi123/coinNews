package com.example.coinnews.model

import java.time.LocalDateTime

data class Article(
    val id: String,
    val title: String,
    val description: String,
    val url: String,
    val author: String? = null,
    val createdAt: Long? = null
)

data class ArticleWithInterest(
    val article: Article,
    val isInterested: Boolean
)

