package com.hong7.coinnews.network.retrofit

import com.hong7.coinnews.network.model.response.GetVideoListResponse
import retrofit2.http.GET
import retrofit2.http.Query


interface YoutubeService {

    @GET("playlistItems")
    suspend fun getYoutubeVideoItems(
        @Query("key") apiKey: String = "AIzaSyCz7C2RD3Y3WK8uhVcMiY_zvuLR63dwWiQ",
        @Query("part") part: String = "snippet",
        @Query("playlistId") playListId: String,
        @Query("pageToken") pageToken: String,
        @Query("maxResults") maxResults: Int = 10
    ): GetVideoListResponse
}