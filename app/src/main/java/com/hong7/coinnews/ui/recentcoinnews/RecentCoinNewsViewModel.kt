package com.hong7.coinnews.ui.recentcoinnews

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hong7.coinnews.data.repository.FilterRepository
import com.hong7.coinnews.data.repository.NewsRepository
import com.hong7.coinnews.model.Article
import com.hong7.coinnews.model.Coin
import com.hong7.coinnews.model.Filter
import com.hong7.coinnews.model.NetworkResult
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
class RecentCoinNewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
) : ViewModel() {

    private val _articles = MutableStateFlow<List<Article>>(emptyList())
    val articles: StateFlow<List<Article>> = _articles.asStateFlow()

    private val _loading = MutableStateFlow<Boolean>(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _watchedNewsIds = MutableStateFlow<Set<String>>(emptySet())
    val watchedNewsIds: StateFlow<Set<String>> = _watchedNewsIds.asStateFlow()

    init {
        viewModelScope.launch {
            val query = "암호화폐"

            _loading.value = true
            val result = newsRepository.getRecentNews(query)
            if (result is NetworkResult.Success)
                _articles.value = result.toModel()
            else return@launch
            _loading.value = false
        }
    }

    fun onNewsClick(newsId: String) {
        val watchedNewsIds = _watchedNewsIds.value.toMutableSet()
        watchedNewsIds.add(newsId)
        _watchedNewsIds.value = watchedNewsIds
    }
}
