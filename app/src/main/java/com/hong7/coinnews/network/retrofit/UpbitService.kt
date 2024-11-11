package com.hong7.coinnews.network.retrofit

import com.hong7.coinnews.network.model.NetworkCoin
import com.hong7.coinnews.network.model.NetworkCoinPrice
import com.hong7.coinnews.network.model.response.CandleResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UpbitService {

    @GET("v1/market/all/")
    suspend fun fetchCoinList(): List<NetworkCoin>

    @GET("v1/ticker")
    suspend fun fetchCoinPrice(
        @Query("markets") markets: String
    ): List<NetworkCoinPrice>

    @GET("v1/candles/minutes/{unit}")
    suspend fun fetchLatestCandles(
        @Path("unit") unit: Int = 240,
        @Query("market") market: String,
        @Query("count") count: Int = 200
    ): List<CandleResponse>
}