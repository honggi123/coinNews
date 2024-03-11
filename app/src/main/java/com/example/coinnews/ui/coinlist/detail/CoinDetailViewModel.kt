package com.example.coinnews.ui.coinlist.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.coinnews.data.repository.CoinRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CoinDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: CoinRepository
) : ViewModel() {

    private val coinId: String = savedStateHandle.get<String>(COIN_ID_SAVED_STATE_KEY)!!

    val coinInfo = repository.getCoinInfo(coinId)

    fun toggleInterest(){

    }

    companion object {
        private const val COIN_ID_SAVED_STATE_KEY = "coinId"
    }
}