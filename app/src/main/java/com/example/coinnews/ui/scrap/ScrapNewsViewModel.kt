package com.example.coinnews.ui.scrap

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coinnews.data.repository.CoinRepository
import com.example.coinnews.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ScrapNewsViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    val news = userRepository.getAllNews().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(1_000),
        emptyList()
    )

//    fun deleteInterest(coin: Coin) {
//        viewModelScope.launch {
//            repository.deleteInterest(coin)
//        }
//    }
}