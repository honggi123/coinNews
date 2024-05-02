package com.hong7.coinnews.network.okhttp.retrofit

import com.hong7.coinnews.BuildConfig
import com.hong7.coinnews.network.model.ArticlesResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface NaverService {

    @GET("/v1/search/news.json")
    suspend fun getArticles(
        @Header("X-Naver-Client-Id") clientId: String = BuildConfig.NAVER_API_KEY,
        @Header("X-Naver-Client-Secret") clientSecret: String = BuildConfig.NAVER_API_SECRETE,
        @Query("query") query: String,
        @Query("start") page: Int,
        @Query("display") pageSize: Int,
    ): ArticlesResponse
}
