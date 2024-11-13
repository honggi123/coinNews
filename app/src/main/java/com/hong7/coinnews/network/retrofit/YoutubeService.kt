package com.hong7.coinnews.network.retrofit

import com.hong7.coinnews.BuildConfig
import com.hong7.coinnews.network.model.response.GetVideoListResponse
import retrofit2.http.GET
import retrofit2.http.Query


interface YoutubeService {

    @GET("playlistItems")
    suspend fun getYoutubeVideoItems(
        @Query("key") apiKey: String = BuildConfig.YOUTUBE_API_KEY,
        @Query("part") part: String = "snippet",
        @Query("playlistId") playListId: String,
        @Query("pageToken") pageToken: String,
        @Query("maxResults") maxResults: Int = 10
    ): GetVideoListResponse
}