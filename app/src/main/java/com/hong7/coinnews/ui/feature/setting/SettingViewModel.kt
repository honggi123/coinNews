package com.hong7.coinnews.ui.feature.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hong7.coinnews.preference.PreferenceManager
import com.hong7.coinnews.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val preferenceManager: PreferenceManager
) : BaseViewModel() {

    val priceAlertEnabled = preferenceManager.getCoinPriceChangeAlertEnabled()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            false
        )

    val volumeAlertEnabled = preferenceManager.getCoinVolumeAlertEnabled()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            false
        )

    fun togglePriceAlertEnabled(enabled: Boolean) {
        viewModelScope.launch {
            preferenceManager.putCoinPriceChangeAlertEnabled(enabled)
        }
    }

    fun toggleVolumeAlertEnabled(enabled: Boolean) {
        viewModelScope.launch {
            preferenceManager.putCoinVolumeAlertEnabled(enabled)
        }
    }
}