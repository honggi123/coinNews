package com.hong7.coinnews.ui.feature.filtersetting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hong7.coinnews.data.repository.FilterRepository
import com.hong7.coinnews.model.Coin
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
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
    val coins: StateFlow<List<Coin>> = filterRepository.getFilter()
        .flatMapLatest { filter ->
            filterRepository.getUserFilter().map {
                val filterCoinIds = it?.coins?.map { it.id }?.toSet() ?: emptySet()
                filter.coins.map { coin ->
                    coin.copy(isSelected = coin.id in filterCoinIds)
                }
            }
        }
        .filterNotNull()
        .catch { TODO() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(3_000),
            emptyList()
        )

    fun onSelectCompleted(selectedCoins: List<Coin>) {
        viewModelScope.launch {
            filterRepository.setMyCoins(selectedCoins)
        }
    }
}
