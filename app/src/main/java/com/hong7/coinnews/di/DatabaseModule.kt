package com.hong7.coinnews.di

import android.content.Context
import com.hong7.coinnews.database.AppDatabase
import com.hong7.coinnews.database.FilterDao
import com.hong7.coinnews.database.NewsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DataBaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Provides
    fun provideCoinFilterDao(appDatabase: AppDatabase): FilterDao {
        return appDatabase.filterDao()
    }

    @Provides
    fun provideNewsDao(appDatabase: AppDatabase): NewsDao {
        return appDatabase.newsDao()
    }
}

