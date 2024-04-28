package com.hong7.coinnews.ui.articlelist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.hong7.coinnews.data.repository.FilterRepository
import com.hong7.coinnews.data.repository.NewsRepository
import com.hong7.coinnews.model.Article
import com.hong7.coinnews.model.Coin
import com.hong7.coinnews.model.Filter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticleListViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    private val filterRepository: FilterRepository
) : ViewModel() {

    val filter: StateFlow<Filter?> = filterRepository.getFilterStream()
        .onEach { filter ->
            if (selectedCoin.value == null && filter != null) {
                changeSelectedCoin(filter.coins.getOrNull(0))
            }
        }.stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            null
        )

    private val _selectedCoin = MutableStateFlow<Coin?>(null)
    val selectedCoin: StateFlow<Coin?> = _selectedCoin.asStateFlow()

    val articles: StateFlow<List<Article>> =
        combine(
            filter.filterNotNull(),
            selectedCoin.filterNotNull()
        ) { filter, selectedCoin ->
            newsRepository.getRecentArticles(selectedCoin)
        }
            .stateIn(
                viewModelScope,
                SharingStarted.Lazily,
                emptyList()
            )

    fun onCoinClick(coin: Coin?) {
        viewModelScope.launch {
            changeSelectedCoin(coin)
        }
    }

    private fun changeSelectedCoin(coin: Coin?) {
        if (coin != null) {
            _selectedCoin.value = coin
        }
    }
}





