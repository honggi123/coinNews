package com.hong7.coinnews.di

import com.hong7.coinnews.network.retrofit.BithumbService
import com.hong7.coinnews.network.retrofit.GoogleCloudService
import com.hong7.coinnews.network.retrofit.NaverService
import com.hong7.coinnews.network.retrofit.UpbitService
import com.hong7.coinnews.network.retrofit.YoutubeService
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
import retrofit2.converter.gson.GsonConverterFactory
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

    @Singleton
    @Provides
    fun provideNewsService(client: OkHttpClient): NaverService {
        val json = Json { ignoreUnknownKeys = true }
        val contentType = "application/json".toMediaType()
        val BASE_URL = "https://openapi.naver.com/"

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
            .create(NaverService::class.java)
    }

    @Singleton
    @Provides
    fun provideYoutubeService(client: OkHttpClient): YoutubeService {
        val BASE_URL = "https://www.googleapis.com/youtube/v3/"

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(YoutubeService::class.java)
    }

    @Singleton
    @Provides
    fun provideUpbitService(client: OkHttpClient): UpbitService {
        val BASE_URL = "https://api.upbit.com/"
        val json = Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
            allowStructuredMapKeys = true
        }
        val contentType = "application/json".toMediaType()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
            .create(UpbitService::class.java)
    }

    @Singleton
    @Provides
    fun provideBithumbService(client: OkHttpClient): BithumbService {
        val BASE_URL = "https://api.bithumb.com/"
        val json = Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
            allowStructuredMapKeys = true
        }
        val contentType = "application/json".toMediaType()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
            .create(BithumbService::class.java)
    }

    @Singleton
    @Provides
    fun provideGoogleCloudService(client: OkHttpClient): GoogleCloudService {
        val BASE_URL = "https://us-central1-coin-news-418815.cloudfunctions.net/"
        val json = Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
            allowStructuredMapKeys = true
        }
        val contentType = "application/json".toMediaType()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
            .create(GoogleCloudService::class.java)
    }
}