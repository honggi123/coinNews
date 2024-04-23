package com.hong7.coinnews.data.repository.impl

import com.hong7.coinnews.data.repository.NewsRepository
import com.hong7.coinnews.data.repository.UserRepository
import com.hong7.coinnews.database.UserNewsDao
import com.hong7.coinnews.network.retrofit.CryptoNewsService
import com.hong7.coinnews.network.retrofit.NaverService
import com.hong7.coinnews.preference.PreferenceManager
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton
@Singleton
class UserRepositoryImpl @Inject constructor(
    private val preferenceManager: PreferenceManager
) : UserRepository {

    override fun getLastUpdateDate() = preferenceManager.getLastUpdateDate()

    override fun updateLastUpdateDate(date: Calendar) = preferenceManager.setLastUpdateDate(date)
}