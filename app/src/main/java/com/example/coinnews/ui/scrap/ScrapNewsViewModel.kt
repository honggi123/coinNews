package com.example.coinnews.ui.scrap

import androidx.lifecycle.ViewModel
import com.example.coinnews.data.repository.CoinRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ScrapNewsViewModel @Inject constructor(
    private val repository: CoinRepository
) : ViewModel() {

//    val coins = repository.getCoinsInterested()
//
//    fun deleteInterest(coin: Coin) {
//        viewModelScope.launch {
//            repository.deleteInterest(coin)
//        }
//    }
}