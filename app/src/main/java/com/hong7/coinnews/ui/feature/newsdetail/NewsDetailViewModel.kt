package com.hong7.coinnews.ui.feature.newsdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.crashlytics.crashlytics
import com.hong7.coinnews.data.repository.NewsRepository
import com.hong7.coinnews.model.News
import com.hong7.coinnews.model.NewsWithInterest
import com.hong7.coinnews.model.Coin
import com.hong7.coinnews.model.exception.NetworkDisconnectedException
import com.hong7.coinnews.ui.NewsDetailNav
import com.hong7.coinnews.ui.feature.mycoinnews.MyCoinNewsUiState
import com.hong7.coinnews.utils.GsonUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class NewsDetailViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val news = savedStateHandle.getStateFlow(ARTICLE_KEY, "")
        .map { GsonUtils.fromJson<News>(it) }

    val uiState: StateFlow<NewsDetailUiState> = news
        .map {  news ->
            news?.let { NewsDetailUiState.Success(news) }
                ?: throw NullPointerException()
        }
        .catch { throwable ->
            Firebase.crashlytics.recordException(throwable)
            val exception = when(throwable){
                is IOException -> NetworkDisconnectedException()
                else -> throwable
            }
            NewsDetailUiState.Failed(exception)
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            NewsDetailUiState.Loading
        )

    val isScraped: StateFlow<Boolean> = news
        .flatMapLatest { news ->
            news?.let { newsRepository.isNewsScraped(news.id) }
                ?: throw NullPointerException()
        }
        .catch {  }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            false
        )

    fun onToggleClick(newsWithInterest: NewsWithInterest) {
        viewModelScope.launch {
            if (newsWithInterest.isInterested) {
                newsRepository.deleteNewsScraped(newsWithInterest.news)
            } else {
                newsRepository.addNewsScraped(newsWithInterest.news)
            }
        }
    }
}

sealed interface NewsDetailUiState {
    object Loading : NewsDetailUiState

    data class Success(
        val news: News
    ) : NewsDetailUiState

    data class Failed(
        val throwable: Throwable
    ) : NewsDetailUiState
}

private const val ARTICLE_KEY = "news"
