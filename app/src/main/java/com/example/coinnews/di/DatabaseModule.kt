package com.example.coinnews.di

import android.content.Context
import com.example.coinnews.database.AppDatabase
import com.example.coinnews.database.CoinFilterDao
import com.example.coinnews.database.NewsDao
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
    fun provideCoinFilterDao(appDatabase: AppDatabase): CoinFilterDao {
        return appDatabase.coinFilterDao()
    }

    @Provides
    fun provideNewsDao(appDatabase: AppDatabase): NewsDao {
        return appDatabase.newsDao()
    }
}

