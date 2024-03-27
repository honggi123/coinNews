package com.example.coinnews.ui.articlelist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.coinnews.data.repository.NewsRepository
import com.example.coinnews.model.Coin
import com.example.coinnews.model.CoinFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ArticleListViewModel @Inject constructor(
    private val repository: NewsRepository
) : ViewModel() {

    private val _selectedCoinFilter = MutableStateFlow(CoinFilter.Bitcoin)
    val selectedCoinFilter: StateFlow<CoinFilter> = _selectedCoinFilter.asStateFlow()

    val articles = selectedCoinFilter
        .flatMapLatest { repository.getArticles(it) }
        .cachedIn(viewModelScope)

    fun updateFilter(filter: CoinFilter) {
        // todo
        _selectedCoinFilter.value = filter
    }
}




