package com.hong7.coinnews.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CoinInfoResponse(
    @SerialName("data") val items: Map<String, NetworkCoinInfo>,
    val status: NetworkStatus
)

@Serializable
data class NetworkCoinInfo(
    val urls: NetworkUrls,
    val logo: String,
    val id: Int,
    val name: String,
    val symbol: String,
    val slug: String,
    val description: String?,
    @SerialName("date_added") val dateAdded: String,
    @SerialName("date_launched") val dateLaunched: String?,
    val tags: List<String>?,
    val category: String?,
    val notice: String?,
)

@Serializable
data class NetworkUrls(
    @SerialName("website") val websiteUrls: List<String>,
    @SerialName("technical_doc") val technicalDocs: List<String>,
    @SerialName("twitter") val twitterUrls: List<String>,
    @SerialName("reddit") val redditUrls: List<String>,
    @SerialName("message_board") val messageBoard: List<String>,
    @SerialName("announcement") val announcements: List<String>,
    val chat: List<String>,
    val explorer: List<String>,
    @SerialName("source_code") val sourceCode: List<String>
)
