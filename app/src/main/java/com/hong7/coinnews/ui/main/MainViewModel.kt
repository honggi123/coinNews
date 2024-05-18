package com.hong7.coinnews.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.hong7.coinnews.data.repository.UserRepository
import com.hong7.coinnews.model.NetworkState
import com.hong7.coinnews.utils.NetworkChecker
import com.hong7.coinnews.worker.CoinInfoUpdateWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val workManager: WorkManager,
    private val networkChecker: NetworkChecker
) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _networkState = MutableSharedFlow<NetworkState>(replay = 1)
    val networkState: SharedFlow<NetworkState> = _networkState

    init {
        viewModelScope.launch {
            networkChecker.networkState.collectLatest {
                _networkState.emit(it)
            }
        }
        updateCoinFilterInfo()
    }

    fun onRetry() {
        // Do something.
    }

    // updatedAt은 필터링이 설정될 때, 앱 스플래시 로딩 완료 될 때 업데이트 됨.
    private fun updateCoinFilterInfo() {
        val updatedDate = userRepository.getLastUpdateDate()
        viewModelScope.launch {
            if (updatedDate != null) {
                val workerRequest = CoinInfoUpdateWorker.enqueue(workManager, updatedDate)
                val workInfo = workManager.getWorkInfoByIdLiveData(workerRequest.id).asFlow()
                workInfo.collect {
                    if (it.state == WorkInfo.State.SUCCEEDED) {
                        userRepository.updateLastUpdateDate(Calendar.getInstance())
                        _isLoading.value = false
                    } else if (it.state == WorkInfo.State.FAILED) {
                        _isLoading.value = false
                    }
                }
            } else {
                _isLoading.value = false
            }
        }
    }
}
