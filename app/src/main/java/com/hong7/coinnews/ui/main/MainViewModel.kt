package com.hong7.coinnews.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import com.google.firebase.messaging.FirebaseMessaging
import com.hong7.coinnews.preference.PreferenceManager
import com.hong7.coinnews.worker.InitAllCoinListWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val workManager: WorkManager,
    private val preferenceManager: PreferenceManager
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        viewModelScope.launch {
            initAlertSetting()
            initAllCoinList()
        }
    }

    fun saveFcmToken(token: String) {
        viewModelScope.launch {
            preferenceManager.putFcmToken(token)
        }
    }

    private fun initAlertSetting() {
        viewModelScope.launch {
            preferenceManager.getCoinVolumeAlertRatio().collectLatest { ratioSetting ->
                if (ratioSetting == null) {
                    preferenceManager.putCoinVolumeAlertRatio(5000)
                }
            }
        }

        viewModelScope.launch {
            preferenceManager.getCoinPriceChangeAlertRatio().collectLatest { ratioSetting ->
                if (ratioSetting == null) {
                    preferenceManager.putCoinPriceChangeAlertRatio(30)
                }
            }
        }
    }

    private fun initAllCoinList() {
        viewModelScope.launch {
            val workerRequest = InitAllCoinListWorker.enqueue(workManager)
            val workInfo = workManager.getWorkInfoByIdLiveData(workerRequest.id).asFlow()
            _isLoading.value = false

            workInfo.collectLatest {
                if (it.state.isFinished) {
                    _isLoading.value = false
                }
            }
            _isLoading.value = false
        }
    }
}
