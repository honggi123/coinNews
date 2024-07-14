package com.hong7.coinnews.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GlobalNewsResponse(
    @SerialName("data") val items: List<NetworkGlobalNews>,
    @SerialName("total_pages") val totalPages: Int
)

@Serializable
data class NetworkGlobalNews(
    @SerialName("news_url") val newsUrl: String,
    @SerialName("image_url") val imageUrl: String,
    @SerialName("title") val title: String,
    @SerialName("text") val text: String,
    @SerialName("source_name") val author: String,
    @SerialName("date") val createdAt: String,
    @SerialName("topics") val topics: List<String>,
    @SerialName("sentiment") val sentiment: String,
    @SerialName("type") val type: String,
    @SerialName("tickers") val tickers: List<String>
)