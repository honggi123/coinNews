package com.hong7.coinnews.di

import com.hong7.coinnews.data.repository.CoinRepository
import com.hong7.coinnews.data.repository.NewsRepository
import com.hong7.coinnews.data.repository.UserRepository
import com.hong7.coinnews.data.repository.impl.CoinRepositoryImpl
import com.hong7.coinnews.data.repository.impl.NewsRepositoryImpl
import com.hong7.coinnews.data.repository.impl.UserRepositoryImpl
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

    @Binds
    @Singleton
    fun bindUserRepository(repository: UserRepositoryImpl): UserRepository
}