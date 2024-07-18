package com.hong7.coinnews.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NewssResponse(
    val total: Int,
    val items: List<NetworkNews>,
    @SerialName("start") val page: Int,
    @SerialName("display") val totalPage: Int,
    @SerialName("lastBuildDate") val lastRequestedAt: String,
)

@Serializable
data class NetworkNews(
    val title: String,
    @SerialName("originallink") val originalUrl: String,
    @SerialName("link") val url: String,
    val description: String,
    @SerialName("pubDate") val createdAt: String
)

