package com.hong7.coinnews.ui.feature.deprecated.recentnews

import androidx.compose.foundation.lazy.LazyListState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.hong7.coinnews.data.repository.NewsRepository
import com.hong7.coinnews.model.News
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

private const val CRYPTO_KEYWORD = "crypto | bitcoin | cryptocurrency | ethereum | 코인 | 비트코인 | 암호화폐 | 이더리움"

@HiltViewModel
class RecentNewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
) : ViewModel() {

    val pagingNews: Flow<PagingData<News>>
        get() = newsRepository.getNews(CRYPTO_KEYWORD)
            .cachedIn(viewModelScope)

    private val _watchedNewsIds = MutableStateFlow<Set<String>>(emptySet())
    val watchedNewsIds: StateFlow<Set<String>> = _watchedNewsIds.asStateFlow()

    fun onNewsClick(newsId: String) {
        val watchedNewsIds = watchedNewsIds.value.toMutableSet()
        watchedNewsIds.add(newsId)
        _watchedNewsIds.value = watchedNewsIds
    }
}

