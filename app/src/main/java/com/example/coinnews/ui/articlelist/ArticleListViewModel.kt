package com.example.coinnews.ui.articlelist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.coinnews.data.repository.NewsRepository
import com.example.coinnews.data.repository.UserRepository
import com.example.coinnews.model.Article
import com.example.coinnews.model.Coin
import com.example.coinnews.model.CoinFilter
import com.example.coinnews.model.CountryScope
import com.example.coinnews.model.Filter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Scope

@HiltViewModel
class ArticleListViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    val allFilters = userRepository.getAllFilters()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(1_000),
            null
        )

    val userFilter = userRepository.getAllFilters()
        .map { Filter(coinFilters = it.coinFilters.filter { it.isSelected }, scope = it.scope) }
        .onEach {
            selectedScope = it.scope
            _selectedCoinFilter.value = it.coinFilters.getOrNull(0)
            changeFilter(it.coinFilters.getOrNull(0))
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(1_000),
            null
        )

    private val _selectedCoinFilter = MutableStateFlow<CoinFilter?>(null)
    val selectedCoinFilter: StateFlow<CoinFilter?> = _selectedCoinFilter.asStateFlow()

    private var selectedScope: CountryScope = CountryScope.Local

    private val _articles = MutableStateFlow<PagingData<Article>?>(null)
    val articles: Flow<PagingData<Article>> get() = _articles.filterNotNull()

    init {

    }

    fun onCoinFilterClick(coinFilter: CoinFilter) {
        val filter = selectedCoinFilter.value
        if (filter != null) {
            viewModelScope.launch {
                changeFilter(coinFilter)
            }
        }
    }

    private suspend fun changeFilter(coinFilter: CoinFilter?) {
        if (coinFilter == null) {
            return
        }
        when (selectedScope) {
            CountryScope.Local -> {
                _articles.value =
                    newsRepository.getArticles(coinFilter).cachedIn(viewModelScope).first()
            }

            CountryScope.Global -> {
                _articles.value =
                    newsRepository.getGlobalArticles(coinFilter).cachedIn(viewModelScope).first()
            }
        }
        _selectedCoinFilter.value = coinFilter
    }

    fun saveCoinFilters(filter: Filter) {
        viewModelScope.launch {
            userRepository.updateFilter(filter)
        }
    }
}





