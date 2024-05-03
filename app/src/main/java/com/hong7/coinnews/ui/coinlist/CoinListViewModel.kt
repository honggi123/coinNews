package com.hong7.coinnews.ui.coinlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hong7.coinnews.data.repository.CoinRepository
import com.hong7.coinnews.data.repository.FilterRepository
import com.hong7.coinnews.data.repository.UserRepository
import com.hong7.coinnews.model.Coin
import com.hong7.coinnews.model.NetworkResult
import com.hong7.coinnews.model.toModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class CoinListViewModel @Inject constructor(
    private val coinRepository: CoinRepository,
    private val filterRepository: FilterRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _coins = MutableStateFlow<List<Coin>>(emptyList())
    val coins: StateFlow<List<Coin>> = _coins.asStateFlow()

    init {
        viewModelScope.launch {
            val allCoinsResult = coinRepository.getAllCoins()
            val allCoins =
                if (allCoinsResult is NetworkResult.Success)
                    allCoinsResult.toModel()
                else return@launch
            val filter = filterRepository.getFilter()
            _coins.value = if (filter != null) {
                val filterCoinIds = filter.coins.map { it.id }
                allCoins.map { coin ->
                    coin.copy(isSelected = coin.id in filterCoinIds)
                }
            } else {
                allCoins
            }
        }
    }

    fun onCompleteSelect(selectedCoins: List<Coin>) {
        viewModelScope.launch {
            filterRepository.setCoins(selectedCoins)
            userRepository.updateLastUpdateDate(Calendar.getInstance())
        }
    }
}
