package com.hong7.coinnews.network.retrofit

import com.hong7.coinnews.BuildConfig
import com.hong7.coinnews.network.model.request.SendNotificationRequest
import com.hong7.coinnews.network.model.response.SendPushNotificationResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface GoogleCloudService {

    @POST("sendPushNotification")
    suspend fun sendPushNotificaiton(
        @Body request: SendNotificationRequest
    ): SendPushNotificationResponse
}