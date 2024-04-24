package com.hong7.coinnews.ui.coinlist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hong7.coinnews.data.mapper.toEntity
import com.hong7.coinnews.data.repository.CoinRepository
import com.hong7.coinnews.data.repository.FilterRepository
import com.hong7.coinnews.data.repository.UserRepository
import com.hong7.coinnews.database.CoinEntity
import com.hong7.coinnews.model.Coin
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class CoinListViewModel @Inject constructor(
    private val coinRepository: CoinRepository,
    private val filterRepository: FilterRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    val coins = coinRepository.getAllCoins()
        .map { allCoins ->
            val filter = filterRepository.getFilter()
            if (filter != null) {
                val filterCoinIds = filter.coins.map { it.id }
                allCoins.map { coin ->
                    coin.copy(isSelected = coin.id in filterCoinIds)
                }
            } else {
                allCoins
            }
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(3_000),
            emptyList()
        )

    fun onCompleteSelect(selectedCoins: List<Coin>) {
        viewModelScope.launch {
            filterRepository.setCoins(selectedCoins)
            userRepository.updateLastUpdateDate(Calendar.getInstance())
        }
    }
}