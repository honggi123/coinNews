package com.hong7.coinnews.di

import com.hong7.coinnews.network.retrofit.CoinMarketCapService
import com.hong7.coinnews.network.retrofit.CryptoNewsService
import com.hong7.coinnews.network.retrofit.NaverService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {


    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideCoinService(client: OkHttpClient): CoinMarketCapService {
        val json = Json { ignoreUnknownKeys = true }
        val contentType = "application/json".toMediaType()

        return Retrofit.Builder()
            .baseUrl("https://pro-api.coinmarketcap.com")
            .client(client)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
            .create(CoinMarketCapService::class.java)
    }


    @Singleton
    @Provides
    fun provideNewsService(client: OkHttpClient): NaverService {
        val json = Json { ignoreUnknownKeys = true }
        val contentType = "application/json".toMediaType()

        return Retrofit.Builder()
            .baseUrl("https://openapi.naver.com")
            .client(client)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
            .create(NaverService::class.java)
    }

    @Singleton
    @Provides
    fun provideCryptoNewsService(client: OkHttpClient): CryptoNewsService {
        val json = Json { ignoreUnknownKeys = true }
        val contentType = "application/json".toMediaType()

        return Retrofit.Builder()
            .baseUrl("https://cryptonews-api.com")
            .client(client)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
            .create(CryptoNewsService::class.java)
    }
}