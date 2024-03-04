package com.example.coinnews.data.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkArticlesResponse(
    val data: List<NetworkArticle>,
    val status: Status
)

@Serializable
data class NetworkArticle(
    val cover: String,
    val assets: List<NetworkAsset>,
    @SerialName("created_at") val createdAt: String,
    @SerialName("released_at") val releasedAt: String,
    val title: String,
    val subtitle: String,
    val type: String,
    @SerialName("source_name") val sourceName: String,
    @SerialName("source_url") val sourceUrl: String
)

@Serializable
data class NetworkAsset(
    val id: Int,
    val name: String,
    val symbol: String,
    val slug: String
)