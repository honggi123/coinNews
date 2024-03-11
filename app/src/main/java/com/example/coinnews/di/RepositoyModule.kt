package com.example.coinnews.di

import com.example.coinnews.data.repository.CoinRepository
import com.example.coinnews.data.repository.NewsRepository
import com.example.coinnews.data.repository.impl.CoinRepositoryImpl
import com.example.coinnews.data.repository.impl.NewsRepositoryImpl
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
    fun bindCoinRepository(repository: CoinRepositoryImpl): CoinRepository
}