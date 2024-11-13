package com.hong7.coinnews.preference

import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import java.util.Calendar
import javax.inject.Inject

class PreferenceManager @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    companion object {
        private const val PREFERENCE_NAME = "preference_name"
        private val Context.dataStore by preferencesDataStore(name = PREFERENCE_NAME)

        private val FIREBASE_TOKEN_KEY = stringPreferencesKey("firebase_token")
        private val COIN_VOLUME_ALERT_ENABLED_KEY = booleanPreferencesKey("COIN_VOLUME_ALERT_ONOFF")
        private val COIN_PRICE_ALERT_ENABLED_KEY = booleanPreferencesKey("COIN_PRICE_PERCENTAGE_ALERT_ONOFF")

        private val COIN_VOLUME_ALERT_RATIO_KEY = intPreferencesKey("COIN_VOLUME_ALERT_RATIO_KEY")
        private val COIN_PRICE_ALERT_RATIO_KEY = intPreferencesKey("COIN_PRICE_ALERT_RATIO_KEY")
    }

    suspend fun putCoinVolumeAlertEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[COIN_VOLUME_ALERT_ENABLED_KEY] = enabled
        }
    }

    suspend fun putCoinPriceChangeAlertEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[COIN_PRICE_ALERT_ENABLED_KEY] = enabled
        }
    }

    suspend fun putCoinPriceChangeAlertRatio(value: Int) {
        context.dataStore.edit { preferences ->
            preferences[COIN_PRICE_ALERT_RATIO_KEY] = value
        }
    }

    suspend fun putCoinVolumeAlertRatio(value: Int) {
        context.dataStore.edit { preferences ->
            preferences[COIN_VOLUME_ALERT_RATIO_KEY] = value
        }
    }

    suspend fun putFcmToken(data: String) {
        context.dataStore.edit { preferences ->
            preferences[FIREBASE_TOKEN_KEY] = data
        }
    }

    fun getFcmToken(): Flow<String> {
        return context.dataStore.data.map { preferences ->
            preferences[FIREBASE_TOKEN_KEY] ?: throw NullPointerException()
        }.distinctUntilChanged()
    }

    fun getCoinVolumeAlertEnabled(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[COIN_VOLUME_ALERT_ENABLED_KEY] ?: false
        }.distinctUntilChanged()
    }

    fun getCoinPriceChangeAlertEnabled(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[COIN_PRICE_ALERT_ENABLED_KEY] ?: false
        }.distinctUntilChanged()
    }

    fun getCoinPriceChangeAlertRatio(): Flow<Int?> {
        return context.dataStore.data.map { preferences ->
            preferences[COIN_PRICE_ALERT_RATIO_KEY]
        }.distinctUntilChanged()
    }

    fun getCoinVolumeAlertRatio(): Flow<Int?> {
        return context.dataStore.data.map { preferences ->
            preferences[COIN_VOLUME_ALERT_RATIO_KEY]
        }.distinctUntilChanged()
    }
}