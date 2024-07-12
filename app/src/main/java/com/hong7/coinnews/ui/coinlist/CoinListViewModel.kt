package com.hong7.coinnews.ui.coinlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hong7.coinnews.data.repository.CoinRepository
import com.hong7.coinnews.data.repository.FilterRepository
import com.hong7.coinnews.model.Coin
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class CoinListViewModel @Inject constructor(
    private val coinRepository: CoinRepository,
    private val filterRepository: FilterRepository,
) : ViewModel() {

    val coins: StateFlow<List<Coin>> = coinRepository.getAllCoins()
        .flatMapLatest { coins ->
            filterRepository.getFilter().map {
                val filterCoinIds = it?.coins?.map { it.id }?.toSet() ?: emptySet()
                it?.coins?.map { coin ->
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

    fun onCompleteSelect(selectedCoins: List<Coin>) {
        viewModelScope.launch {
            filterRepository.setMyCoins(selectedCoins)
        }
    }
}
