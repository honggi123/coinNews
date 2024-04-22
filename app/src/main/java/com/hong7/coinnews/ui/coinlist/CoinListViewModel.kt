package com.hong7.coinnews.ui.coinlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hong7.coinnews.data.mapper.toEntity
import com.hong7.coinnews.data.repository.CoinRepository
import com.hong7.coinnews.data.repository.FilterRepository
import com.hong7.coinnews.database.CoinEntity
import com.hong7.coinnews.model.Coin
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoinListViewModel @Inject constructor(
    private val coinRepository: CoinRepository,
    private val filterRepository: FilterRepository
) : ViewModel() {

    val coins = combine(
        coinRepository.getAllCoins(),
        filterRepository.getFilter()
    ) { allCoins, filter ->
        val filterCoinIds = filter.coins.map { it.id }
        allCoins.map { coin ->
            coin?.copy(isSelected = coin.id in filterCoinIds)
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(3_000),
        null
    )

    fun onCompleteSelect(coins: List<Coin>) {
        val newList = coins.filter { it.isSelected }

        viewModelScope.launch {
            filterRepository.setCoins(newList)
        }
    }
}