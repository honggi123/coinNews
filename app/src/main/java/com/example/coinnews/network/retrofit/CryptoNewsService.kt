package com.example.coinnews.network.retrofit

import com.example.coinnews.network.model.CoinsResponse
import com.example.coinnews.network.model.GlobalArticleResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface CryptoNewsService {
    @GET("/api/v1")
    suspend fun getCoinNews(
        @Query("tickers") tickers: String,
        @Query("items") pageSize: Int,
        @Query("page") page: Int,
        @Query("token") token: String = "yoi3keaxohypihvjcnkqqju637dhie3trq5ymyqt",
    ): GlobalArticleResponse
}