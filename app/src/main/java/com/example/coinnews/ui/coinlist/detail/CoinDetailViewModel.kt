package com.example.coinnews.ui.coinlist.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coinnews.data.repository.CoinRepository
import com.example.coinnews.model.Coin
import com.example.coinnews.model.CoinWithInterest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CoinDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: CoinRepository
) : ViewModel() {

    private val coinId: String = savedStateHandle.get<String>(COIN_ID_SAVED_STATE_KEY)!!

    val isInterested = repository.isInterested(coinId)
    val coinInfo = repository.getCoinInfo(
        id = coinId,
        onError = { message -> Timber.d(message) }
    )

    fun toggleInterest(coinWithInterest: CoinWithInterest) {
        viewModelScope.launch {
            if (coinWithInterest.isInterested) {
                repository.deleteInterest(coinWithInterest.coin)
            } else {
                repository.addInterest(coinWithInterest.coin)
            }
        }
    }

    companion object {
        private const val COIN_ID_SAVED_STATE_KEY = "coinId"
    }
}


