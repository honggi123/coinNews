package com.hong7.coinnews.ui.articlelist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.hong7.coinnews.data.repository.NewsRepository
import com.hong7.coinnews.data.repository.UserRepository
import com.hong7.coinnews.model.Article
import com.hong7.coinnews.model.CoinFilter
import com.hong7.coinnews.model.CountryScope
import com.hong7.coinnews.model.Filter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticleListViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    val allFilters = userRepository.getAllFilters()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(3_000),
            null
        )

    val userFilter: StateFlow<Filter?> = userRepository.getAllFilters()
        .map { it?.copy(coinFilters = it.coinFilters.filter { it.isSelected }) }
        .filterNotNull()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(3_000),
            null
        )

    private val _selectedCoinFilter = MutableStateFlow<CoinFilter?>(null)
    val selectedCoinFilter: StateFlow<CoinFilter?> = _selectedCoinFilter.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val articles: Flow<PagingData<Article>> =
        combine(
            userFilter.filterNotNull(),
            selectedCoinFilter.filterNotNull()
        ) { filter, selectedCoinFilter ->
            when (filter.scope) {
                CountryScope.Local -> newsRepository.getArticles(selectedCoinFilter)
                CountryScope.Global -> newsRepository.getGlobalArticles(selectedCoinFilter)
            }
        }.flatMapLatest { it.cachedIn(viewModelScope) }

    fun onCoinFilterClick(coinFilter: CoinFilter) {
        viewModelScope.launch {
            changeFilter(coinFilter)
        }
    }

    private fun changeFilter(coinFilter: CoinFilter) {
        _selectedCoinFilter.value = coinFilter
    }

    fun saveCoinFilters(filter: Filter) {
        viewModelScope.launch {
            userRepository.updateFilter(filter)
        }
    }

    fun refresh(){

    }
}





