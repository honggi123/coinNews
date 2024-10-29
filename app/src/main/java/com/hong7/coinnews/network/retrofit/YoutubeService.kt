package com.hong7.coinnews.network.retrofit

import com.hong7.coinnews.network.model.response.VideoListResponse
import retrofit2.http.GET
import retrofit2.http.Query


interface YoutubeService {

    @GET("search")
    suspend fun getYoutubeVideoItems(
        @Query("key") apiKey: String = "AIzaSyCz7C2RD3Y3WK8uhVcMiY_zvuLR63dwWiQ",
        @Query("part") part: String = "snippet",
        @Query("q") query: String,
        @Query("pageToken") pageToken: String,
        @Query("videoDefinition") videoDefinition: String = "any",
        @Query("videoType") videoType: String = "any",
        @Query("type") type: String = "video"
    ): VideoListResponse
}