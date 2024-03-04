package com.example.coinnews.data.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Status(
    val timestamp: String,
    @SerialName("error_code") val errorCode: Int,
    @SerialName("error_message") val errorMessage: String,
    val elapsed: Int,
    @SerialName("credit_count") val creditCount: Int
)