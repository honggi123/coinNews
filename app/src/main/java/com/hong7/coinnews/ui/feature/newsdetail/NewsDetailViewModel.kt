package com.hong7.coinnews.ui.feature.newsdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.crashlytics.crashlytics
import com.hong7.coinnews.data.repository.NewsRepository
import com.hong7.coinnews.model.Article
import com.hong7.coinnews.model.ArticleWithInterest
import com.hong7.coinnews.model.Coin
import com.hong7.coinnews.ui.NewsDetailNav
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
import javax.inject.Inject

@HiltViewModel
class NewsDetailViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val article = savedStateHandle.getStateFlow(ARTICLE_KEY, "")
        .map { GsonUtils.fromJson<Article>(it) }

    val uiState: StateFlow<NewsDetailUiState> = article
        .map {  article ->
            article?.let { NewsDetailUiState.Success(article) }
                ?: throw NullPointerException()
        }
        .catch {
            Firebase.crashlytics.recordException(it)
            NewsDetailUiState.Failed(it)
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            NewsDetailUiState.Loading
        )

    val isScraped: StateFlow<Boolean> = article
        .flatMapLatest { article ->
            article?.let { newsRepository.isNewsScraped(article.id) }
                ?: throw NullPointerException()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(3_000),
            false
        )

    fun onToggleClick(articleWithInterest: ArticleWithInterest) {
        viewModelScope.launch {
            if (articleWithInterest.isInterested) {
                newsRepository.deleteNewsScraped(articleWithInterest.article)
            } else {
                newsRepository.addNewsScraped(articleWithInterest.article)
            }
        }
    }
}

sealed interface NewsDetailUiState {
    object Loading : NewsDetailUiState

    data class Success(
        val article: Article
    ) : NewsDetailUiState

    data class Failed(
        val throwable: Throwable
    ) : NewsDetailUiState
}

private const val ARTICLE_KEY = "article"
