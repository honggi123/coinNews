package com.hong7.coinnews.ui.feature.watchlist.search

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.hong7.coinnews.data.repository.CoinRepositoy
import com.hong7.coinnews.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SearchWatchListViewModel @Inject constructor(
    private val coinRepositoy: CoinRepositoy
): BaseViewModel() {

    private val _query: MutableStateFlow<String> = MutableStateFlow("")
    val query: StateFlow<String> = _query

    val searchedCoins = query.flatMapLatest {
        coinRepositoy.getCoinListByQuery(it)
    }.
    onEach { Timber.i(it.toString()) }.
    stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    fun onQueryChanged(value: String) {
        viewModelScope.launch {
            _query.value = value
        }
    }

}