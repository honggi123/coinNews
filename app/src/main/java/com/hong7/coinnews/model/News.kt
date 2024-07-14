package com.hong7.coinnews.model

import java.time.LocalDateTime

data class News(
    val id: String,
    val title: String,
    val description: String = "",
    val url: String,
    val author: String? = null,
    val createdAt: LocalDateTime? = null
)

data class NewsWithInterest(
    val news: News,
    val isInterested: Boolean
)

