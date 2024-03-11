package com.example.coinnews.network.retrofit

import com.example.coinnews.network.model.CoinInfoResponse
import com.example.coinnews.network.model.CoinsResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface CoinService {

    @GET("/v1/cryptocurrency/listings/latest")
    suspend fun getCoins(
        @Header("X-CMC_PRO_API_KEY") key: String = "a164184f-4e7f-4424-94ec-cc5437304222",
        @Query("start") page: Int,
        @Query("limit") pageSize: Int,
        @Query("sort") sortOption: String = "market_cap",
        @Query("sort_dir") sort: String = "asc"
    ): CoinsResponse

    @GET("/v2/cryptocurrency/info")
    suspend fun getCoinInfo(
        @Header("X-CMC_PRO_API_KEY") key: String = "a164184f-4e7f-4424-94ec-cc5437304222",
        @Query("id") id: String,
    ): CoinInfoResponse
}