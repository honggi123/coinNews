package com.example.coinnews.di

import android.content.Context
import com.example.coinnews.database.AppDatabase
import com.example.coinnews.database.CoinInterestedDao
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
    fun provideCoinInterestedDao(appDatabase: AppDatabase): CoinInterestedDao {
        return appDatabase.coinFollowedDao()
    }
}

