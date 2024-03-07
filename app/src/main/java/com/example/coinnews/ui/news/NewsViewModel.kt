package com.example.coinnews.ui.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.coinnews.data.repository.NewsRepository
import com.example.coinnews.model.Coin
import com.example.coinnews.model.CoinSortOption
import com.example.coinnews.model.Sort
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val repository: NewsRepository
) : ViewModel() {

    private val defaultSortOptions = mapOf(
        CoinSortOption.MarketCap to Sort.None,
        CoinSortOption.Rank to Sort.None,
        CoinSortOption.PriceChange24h to Sort.None
    )

    private val _coinSortOptions = MutableStateFlow(defaultSortOptions)
    val coinSortOptions = _coinSortOptions.asStateFlow()

    private val _coins = MutableStateFlow<PagingData<Coin>?>(null)
    val coins: Flow<PagingData<Coin>> = _coins.filterNotNull()

    val articles = repository.getArticles()
        .cachedIn(viewModelScope)

    fun changeNextCoinSort(option: CoinSortOption, sort: Sort) {
        val newSort = if (sort.step >= 2) {
            Sort.find(0)
        } else {
            Sort.find(sort.step + 1)
        }

        viewModelScope.launch {
            _coins.value = repository.getCoins(option, newSort).cachedIn(viewModelScope).first()
            _coinSortOptions.value = createChangedSortOptions(option, newSort)
        }
    }

    private fun createChangedSortOptions(
        option: CoinSortOption,
        newSort: Sort
    ): Map<CoinSortOption, Sort> {
        val options = defaultSortOptions.toMutableMap() // todo
        return options.apply { this[option] = newSort }
    }
}



