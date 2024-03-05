package com.example.coinnews.data.network.retrofit

import com.example.coinnews.data.network.model.NetworkArticlesResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface NewsService {

    @GET("/v1/search/news.json")
    suspend fun getArticles(
        @Header("X-Naver-Client-Id") clientId: String = "f9SuTUUoL1UuXOADkYgO",
        @Header("X-Naver-Client-Secret") clientSecret: String = "a1NtEPLEQk",
        @Query("query") query: String,
        @Query("start") page: Int,
        @Query("display") pageSize: Int,
    ): NetworkArticlesResponse
}
