package com.hong7.coinnews.network.retrofit

import com.hong7.coinnews.network.model.NetworkCoin
import com.hong7.coinnews.network.model.NetworkCoinPrice
import retrofit2.http.GET
import retrofit2.http.Query

interface UpbitService {

    @GET("v1/market/all/")
    suspend fun fetchCoinList(): List<NetworkCoin>

    @GET("v1/ticker")
    suspend fun fetchCoinPrice(
        @Query("markets") markets: String
    ): List<NetworkCoinPrice>
}