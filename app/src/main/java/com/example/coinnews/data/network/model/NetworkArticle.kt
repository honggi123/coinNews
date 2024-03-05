package com.example.coinnews.data.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class NetworkArticlesResponse(
    val total: Int,
    val items: List<NetworkArticle>,
    @SerialName("start") val page: Int,
    @SerialName("display") val totalPage: Int,
    @SerialName("lastBuildDate") val lastRequestedAt: String,
)

@Serializable
data class NetworkArticle(
    val title: String,
    @SerialName("originallink") val originalUrl: String,
    @SerialName("link") val url: String,
    val description: String,
    @SerialName("pubDate") val createdAt: String
)
