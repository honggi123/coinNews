package com.hong7.coinnews.network.retrofit

import com.hong7.coinnews.BuildConfig
import com.hong7.coinnews.network.model.GlobalArticleResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CryptoNewsService {
    @GET("/api/v1")
    suspend fun getCoinNews(
        @Query("tickers") tickers: String,
        @Query("items") pageSize: Int,
        @Query("page") page: Int,
        @Query("token") apiKey: String = BuildConfig.CRTPTO_NEWS_API_KEY,
    ): GlobalArticleResponse
}