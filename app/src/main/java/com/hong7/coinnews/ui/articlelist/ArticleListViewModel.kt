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
import com.hong7.coinnews.model.mapNetworkResult
import com.hong7.coinnews.model.toModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
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
import kotlinx.coroutines.withContext
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

    private val _articles = MutableStateFlow<List<Article>>(emptyList())
    val articles: StateFlow<List<Article>> = _articles.asStateFlow()

    private val _loading = MutableStateFlow<Boolean>(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    fun onCoinClick(coin: Coin?) {
        viewModelScope.launch {
            changeSelectedCoin(coin)
        }
    }

    private fun changeSelectedCoin(coin: Coin?) {
        _loading.value = true
        viewModelScope.launch {
            if (coin != null) {
                _selectedCoin.value = coin
                _articles.value = getRecentArticles(coin)
                _loading.value = false
            }
        }
    }

    private suspend fun getRecentArticles(coin: Coin): List<Article> =
        withContext(Dispatchers.Default) {
            val searchWordsWithPlus = coin.relatedSearchWord.joinToString(separator = " | ")
            val query = "${coin.name} | ${searchWordsWithPlus}"

            val naverNewsDeffered = async { newsRepository.getNaverNews(query) }
            val googleNewsDeffered = async { newsRepository.getGoogleNews(coin.name) }

            (naverNewsDeffered.await().toModel() + googleNewsDeffered.await().toModel())
                .distinctBy { it.id }
                .sortedByDescending { it.createdAt }
        }
}





