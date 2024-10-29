package com.hong7.coinnews.ui.feature.filtersetting

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.crashlytics.crashlytics
import com.hong7.coinnews.data.repository.FilterRepository
import com.hong7.coinnews.model.Coin
import com.hong7.coinnews.model.Filter
import com.hong7.coinnews.model.exception.ResponseResource
import com.hong7.coinnews.model.exception.UnknownException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.ConnectException
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class FilterSettingViewModel @Inject constructor(
    private val filterRepository: FilterRepository,
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<FilterSettingUiState> = filterRepository.getFilter()
        .flatMapLatest { filter ->
            filterRepository.getUserFilter().map {
                when(filter){
                    is ResponseResource.Success -> {
                        val selectedCoinIds = it?.coins?.map { it.id }?.toSet() ?: emptySet()
                        val coins = filter.data.coins.map { coin ->
                            coin.copy(isSelected = coin.id in selectedCoinIds)
                        }
                        FilterSettingUiState.Success(coins)
                    }
                    is ResponseResource.Loading -> {
                        FilterSettingUiState.Loading
                    }
                    is ResponseResource.Error -> {
                        FilterSettingUiState.Failed(filter.exception)
                    }
                }
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            FilterSettingUiState.Loading
        )

    fun onSelectCompleted(selectedCoins: List<Coin>) {
        viewModelScope.launch {
            filterRepository.setMyCoinsFilter(selectedCoins)
        }
    }
}

sealed interface FilterSettingUiState {
    object Loading : FilterSettingUiState

    data class Success(
        val items: List<Coin>
    ) : FilterSettingUiState

    data class Failed(
        val throwable: Throwable
    ) : FilterSettingUiState
}
