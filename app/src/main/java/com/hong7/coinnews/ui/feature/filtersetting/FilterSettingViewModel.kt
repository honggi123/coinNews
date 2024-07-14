package com.hong7.coinnews.ui.feature.filtersetting

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.crashlytics.crashlytics
import com.hong7.coinnews.data.repository.FilterRepository
import com.hong7.coinnews.model.Coin
import com.hong7.coinnews.model.Filter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilterSettingViewModel @Inject constructor(
    private val filterRepository: FilterRepository,
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<FilterSettingUiState> = filterRepository.getFilter()
        .flatMapLatest<Filter, FilterSettingUiState> { filter ->
            filterRepository.getUserFilter().map {
                val selectedCoinIds = it?.coins?.map { it.id }?.toSet() ?: emptySet()
                val coins = filter.coins.map { coin ->
                    coin.copy(isSelected = coin.id in selectedCoinIds)
                }
                FilterSettingUiState.Success(coins)
            }
        }
        .catch {
            Firebase.crashlytics.recordException(it)
            emit(FilterSettingUiState.Failed(it))
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            FilterSettingUiState.Loading
        )

    fun onSelectCompleted(selectedCoins: List<Coin>) {
        viewModelScope.launch {
            filterRepository.setMyCoins(selectedCoins)
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
