package com.example.coinnews.data.network.retrofit

import com.example.coinnews.data.network.model.NetworkArticlesResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface NewsService {

    @GET("/v1/content/latest")
    suspend fun getArticles(
        @Path("start") page: Int,
        @Path("limit") pageSize: Int,
        @Path("content_type") contentType: String,
    ): NetworkArticlesResponse
}
