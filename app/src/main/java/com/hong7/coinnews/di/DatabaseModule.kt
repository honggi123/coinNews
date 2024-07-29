package com.hong7.coinnews.di

import android.content.Context
import com.hong7.coinnews.database.dao.AppDatabase
import com.hong7.coinnews.database.dao.UserFilterDao
import com.hong7.coinnews.database.InterestedNewsDao
import com.hong7.coinnews.database.dao.NewsDao
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
    fun provideUserFilterDao(appDatabase: AppDatabase): UserFilterDao {
        return appDatabase.filterDao()
    }

    @Provides
    fun provideInterestedNewsDao(appDatabase: AppDatabase): InterestedNewsDao {
        return appDatabase.interestedNewsDao()
    }

    @Provides
    fun provideNewsDao(appDatabase: AppDatabase): NewsDao {
        return appDatabase.newsDao()
    }
}

