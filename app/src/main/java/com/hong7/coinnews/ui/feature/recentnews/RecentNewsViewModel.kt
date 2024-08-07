package com.hong7.coinnews.ui.feature.recentnews

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.crashlytics.crashlytics
import com.hong7.coinnews.data.repository.NewsRepository
import com.hong7.coinnews.model.News
import com.hong7.coinnews.model.exception.ResponseResource
import com.hong7.coinnews.model.exception.UnknownException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class RecentNewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
) : ViewModel() {

    val uiState: StateFlow<RecentCoinNewsUiState> =
        newsRepository.getRecentNewsByQuery(CRYPTO_CURRENCY_KEYWORD)
            .map {
                when(it){
                    is ResponseResource.Success -> {
                        RecentCoinNewsUiState.Success(it.data)
                    }
                    is ResponseResource.Error -> {
                        RecentCoinNewsUiState.Failed(it.exception)
                    }
                    is ResponseResource.Loading -> {
                        RecentCoinNewsUiState.Loading
                    }
                }
            }
            .catch { throwable ->
                Firebase.crashlytics.recordException(throwable)
                emit(RecentCoinNewsUiState.Failed(UnknownException()))
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                RecentCoinNewsUiState.Loading
            )

    private val _watchedNewsIds = MutableStateFlow<Set<String>>(emptySet())
    val watchedNewsIds: StateFlow<Set<String>> = _watchedNewsIds.asStateFlow()

    fun onNewsClick(newsId: String) {
        val watchedNewsIds = watchedNewsIds.value.toMutableSet()
        watchedNewsIds.add(newsId)
        _watchedNewsIds.value = watchedNewsIds
    }
}

private const val CRYPTO_CURRENCY_KEYWORD = "암호화폐"

sealed interface RecentCoinNewsUiState {
    object Loading : RecentCoinNewsUiState

    data class Success(
        val newsList: List<News>
    ) : RecentCoinNewsUiState

    data class Failed(
        val throwable: Throwable
    ) : RecentCoinNewsUiState
}
