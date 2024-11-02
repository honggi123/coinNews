package com.hong7.coinnews.network.retrofit

import com.hong7.coinnews.network.model.response.GetAllCoinListResponse
import com.hong7.coinnews.network.model.response.GetCoinQuoteResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface CoinMarketCapService {

    @GET("v1/cryptocurrency/map")
    @Headers("X-CMC_PRO_API_KEY: a164184f-4e7f-4424-94ec-cc5437304222")
    suspend fun fetchCoinList(): GetAllCoinListResponse

    @GET("v2/cryptocurrency/quotes/latest")
    @Headers("X-CMC_PRO_API_KEY: a164184f-4e7f-4424-94ec-cc5437304222")
    suspend fun fetchCoinQuote(
        @Query("id") ids: String,
        @Query("convert") convert: String = "KRW"
    ): GetCoinQuoteResponse
}