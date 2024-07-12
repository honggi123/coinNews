package com.hong7.coinnews.di

import com.hong7.coinnews.data.repository.FilterRepository
import com.hong7.coinnews.data.repository.NewsRepository
import com.hong7.coinnews.data.repository.impl.FilterRepositoryImpl
import com.hong7.coinnews.data.repository.impl.NewsRepositoryImpl
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
    fun bindFilterRepository(repository: FilterRepositoryImpl): FilterRepository
}