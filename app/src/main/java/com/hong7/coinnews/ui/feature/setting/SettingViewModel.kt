package com.hong7.coinnews.ui.feature.setting

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

    val priceRatePercentages = listOf(10, 20, 30)
    val volumeRatePercentages = listOf(1000, 2000, 5000)

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

    val selectedPriceRate = preferenceManager.getCoinPriceChangeAlertRatio()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            false
        )

    val selectedVolumeRate = preferenceManager.getCoinVolumeAlertRatio()
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

    fun selectPriceRate(ratio: Int) {
        viewModelScope.launch {
            preferenceManager.putCoinPriceChangeAlertRatio(ratio)
        }
    }

    fun selectVolumeRate(ratio: Int) {
        viewModelScope.launch {
            preferenceManager.putCoinVolumeAlertRatio(ratio)
        }
    }
}