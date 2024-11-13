package com.hong7.coinnews.network.model.response

import kotlinx.serialization.Serializable

@Serializable
data class SendPushNotificationResponse(
    val success: Boolean,
)