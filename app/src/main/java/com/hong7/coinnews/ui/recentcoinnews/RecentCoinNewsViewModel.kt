package com.hong7.coinnews.ui.recentcoinnews

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hong7.coinnews.data.repository.NewsRepository
import com.hong7.coinnews.model.Article
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class RecentCoinNewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
) : ViewModel() {

    private val _watchedNewsIds = MutableStateFlow<Set<String>>(emptySet())
    val watchedNewsIds: StateFlow<Set<String>> = _watchedNewsIds.asStateFlow()

    val uiState: StateFlow<RecentCoinNewsUiState> = newsRepository.getRecentNewsByQuery("암호화폐")
        .map { RecentCoinNewsUiState.Success(it) }
        .catch { RecentCoinNewsUiState.Failed(it) }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(3_000),
            RecentCoinNewsUiState.Loading
        )

    fun onNewsClick(newsId: String) {
        val watchedNewsIds = _watchedNewsIds.value.toMutableSet()
        watchedNewsIds.add(newsId)
        _watchedNewsIds.value = watchedNewsIds
    }
}

sealed interface RecentCoinNewsUiState {

    object Loading : RecentCoinNewsUiState

    data class Success(
        val newsList: List<Article>
    ) : RecentCoinNewsUiState

    data class Failed(
        val throwable: Throwable
    ) : RecentCoinNewsUiState
}
