package com.hong7.coinnews.network.model.request

import kotlinx.serialization.Serializable

@Serializable
data class SendNotificationRequest(
    val message: String,
    val title: String,
    val token: String
)