package com.hong7.coinnews.di

import com.hong7.coinnews.data.repository.CoinRepositoy
import com.hong7.coinnews.data.repository.NewsRepository
import com.hong7.coinnews.data.repository.VideoRepository
import com.hong7.coinnews.data.repository.WatchListRepository
import com.hong7.coinnews.data.repository.impl.CoinRepositoryImpl
import com.hong7.coinnews.data.repository.impl.NewsRepositoryImpl
import com.hong7.coinnews.data.repository.impl.VideoRepositoryImpl
import com.hong7.coinnews.data.repository.impl.WatchListRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoyModule {

    @Binds
    @Singleton
    fun bindNewsRepository(repository: NewsRepositoryImpl): NewsRepository

    @Binds
    @Singleton
    fun bindVideoRepository(repository: VideoRepositoryImpl): VideoRepository

    @Binds
    @Singleton
    fun bindCoinRepository(repository: CoinRepositoryImpl): CoinRepositoy

    @Binds
    @Singleton
    fun bindWatchListRepository(repository: WatchListRepositoryImpl): WatchListRepository
}