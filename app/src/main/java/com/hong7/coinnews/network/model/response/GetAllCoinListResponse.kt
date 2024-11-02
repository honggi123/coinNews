package com.hong7.coinnews.network.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetAllCoinListResponse(
    val data: List<NetworkCoin>,
    val status: NetworkCoinStatus
)
@Serializable
data class NetworkCoin(
    val id: Int,
    val rank: Int,
    val name: String,
    val symbol: String,
    val slug: String,
    val platform: NetworkPlatform?
)

@Serializable
data class NetworkPlatform(
    val id: Int,
    val name: String,
    val symbol: String,
    val slug: String,
    val token_address: String
)

@Serializable
data class NetworkCoinStatus(
    val timestamp: String,
    @SerialName("error_code")
    val errorCode: Int,
    @SerialName("error_message")
    val errorMessage: String,
    val elapsed: Int,
    @SerialName("credit_count")
    val creditCard: Int
)