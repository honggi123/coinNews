package com.example.coinnews.ui.interest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coinnews.data.repository.CoinRepository
import com.example.coinnews.model.Coin
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InterestCoinViewModel @Inject constructor(
    private val repository: CoinRepository
) : ViewModel() {

    val coins = repository.getCoinsInterested()

    fun deleteInterest(coin: Coin) {
        viewModelScope.launch {
            repository.deleteInterest(coin)
        }
    }
}