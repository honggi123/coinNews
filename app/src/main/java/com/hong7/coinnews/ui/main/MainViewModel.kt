package com.hong7.coinnews.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hong7.coinnews.model.NetworkState
import com.hong7.coinnews.utils.NetworkChecker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val networkChecker: NetworkChecker
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _networkState = MutableSharedFlow<NetworkState>(replay = 1)
    val networkState: SharedFlow<NetworkState> = _networkState

    init {
        viewModelScope.launch {
            networkChecker.networkState.collectLatest {
                _networkState.emit(it)
            }
        }
    }
}
