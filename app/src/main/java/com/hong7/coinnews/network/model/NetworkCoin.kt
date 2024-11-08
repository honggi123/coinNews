package com.hong7.coinnews.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkCoin(
    @SerialName("market")
    val marketId: String,
    @SerialName("korean_name")
    val koreanName: String,
    @SerialName("english_name")
    val englishName: String,
)