package com.hong7.coinnews.ui.mycoinnews

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hong7.coinnews.data.repository.FilterRepository
import com.hong7.coinnews.data.repository.NewsRepository
import com.hong7.coinnews.model.Article
import com.hong7.coinnews.model.Coin
import com.hong7.coinnews.model.Filter
import com.hong7.coinnews.model.NetworkResult
import com.hong7.coinnews.model.NetworkState
import com.hong7.coinnews.model.toModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MyCoinNewsViewModel @Inject constructor(
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

    private val _watchedNewsIds = MutableStateFlow<Set<String>>(emptySet())
    val watchedNewsIds: StateFlow<Set<String>> = _watchedNewsIds.asStateFlow()

    fun onNewsClick(newsId: String) {
        val watchedNewsIds = _watchedNewsIds.value.toMutableSet()
        watchedNewsIds.add(newsId)
        _watchedNewsIds.value = watchedNewsIds
    }

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

    suspend fun getSelectedCoinNews() {
        _articles.value = if (selectedCoin.value == null) {
            emptyList()
        } else {
            getRecentArticles(selectedCoin.value!!)
        }
    }

    private suspend fun getRecentArticles(coin: Coin): List<Article> =
        withContext(Dispatchers.Default) {
            val searchWordsWithPlus = coin.relatedSearchWord.joinToString(separator = " | ")
            val query = "${coin.name} | ${searchWordsWithPlus}"

            val naverNewsDeffered = async { newsRepository.getNaverNews(query) }
            val googleNewsDeffered = async { newsRepository.getGoogleNews(coin.name) }

            val naverNews = naverNewsDeffered.await()
            val googleNews = googleNewsDeffered.await()

            if (naverNews is NetworkResult.Success && googleNews is NetworkResult.Success) {
                (naverNews.toModel() + googleNews.toModel())
                    .distinctBy { it.id }
                    .sortedByDescending { it.createdAt }
            } else {
                emptyList()     // todo
            }
        }
}





