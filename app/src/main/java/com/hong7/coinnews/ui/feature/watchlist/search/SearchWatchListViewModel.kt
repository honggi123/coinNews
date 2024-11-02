package com.hong7.coinnews.ui.feature.watchlist.search

import androidx.lifecycle.viewModelScope
import com.hong7.coinnews.data.repository.CoinRepositoy
import com.hong7.coinnews.data.repository.WatchListRepository
import com.hong7.coinnews.model.Coin
import com.hong7.coinnews.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SearchWatchListViewModel @Inject constructor(
    private val coinRepositoy: CoinRepositoy,
    private val watchListRepository: WatchListRepository
) : BaseViewModel() {

    private val _query: MutableStateFlow<String> = MutableStateFlow("")
    val query: StateFlow<String> = _query

    val watchListCoins = watchListRepository.getWatchList()
        .transform { watchList ->
            emit(watchList.map { it.id }.toSet())
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptySet()
        )

    val searchedCoins = query.flatMapLatest {
        coinRepositoy.getCoinListByQuery(it)
    }
        .onEach { Timber.i(it.toString()) }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    fun onQueryChanged(value: String) {
        viewModelScope.launch {
            _query.value = value
        }
    }

    fun toggleSelected(coin: Coin) {
        viewModelScope.launch {
            if (watchListCoins.value.contains(coin.id)) {
                watchListRepository.removeWatchListCoin(coin)
            } else {
                watchListRepository.addWatchListCoin(coin)
            }
        }
    }
}