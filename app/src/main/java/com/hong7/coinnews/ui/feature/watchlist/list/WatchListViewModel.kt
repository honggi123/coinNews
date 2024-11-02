package com.hong7.coinnews.ui.feature.watchlist.list

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.hong7.coinnews.data.repository.CoinRepositoy
import com.hong7.coinnews.data.repository.WatchListRepository
import com.hong7.coinnews.model.CoinQuote
import com.hong7.coinnews.model.News
import com.hong7.coinnews.model.exception.ResponseResource
import com.hong7.coinnews.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class WatchListViewModel @Inject constructor(
    val watchListRepository: WatchListRepository,
    val coinRepositoy: CoinRepositoy
) : BaseViewModel() {

    val uiState = watchListRepository.getWatchList()
        .flatMapLatest { list ->
            val coinIds = list.map { it.id }
            if (coinIds.isEmpty()) {
                flowOf(ResponseResource.Success(emptyMap()))
            } else {
                coinRepositoy.getCoinQuote(coinIds)
            }
        }.map {
            when (it) {
                is ResponseResource.Loading -> {
                    WatchListUiState.Loading
                }

                is ResponseResource.Success -> {
                    WatchListUiState.Success(it.data)
                }

                is ResponseResource.Error -> {
                    onErrorResonse(it)
                    WatchListUiState.Failed(it.exception)
                }
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            WatchListUiState.Loading
        )
}

sealed interface WatchListUiState {

    object Loading : WatchListUiState

    data class Success(
        val watchList: Map<String, CoinQuote>
    ) : WatchListUiState

    data class Failed(
        val throwable: Throwable
    ) : WatchListUiState
}

