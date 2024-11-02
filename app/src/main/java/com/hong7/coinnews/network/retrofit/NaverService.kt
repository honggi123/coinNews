package com.hong7.coinnews.network.retrofit

import com.hong7.coinnews.BuildConfig
import com.hong7.coinnews.network.model.response.GetNewsResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface NaverService {

    @GET("v1/search/news.json")
    suspend fun fetchNews(
        @Header("X-Naver-Client-Id") clientId: String = BuildConfig.NAVER_API_KEY,
        @Header("X-Naver-Client-Secret") clientSecret: String = BuildConfig.NAVER_API_SECRETE,
        @Query("query") query: String,
        @Query("start") start: Int,
        @Query("display") pageSize: Int,
    ): GetNewsResponse
}
