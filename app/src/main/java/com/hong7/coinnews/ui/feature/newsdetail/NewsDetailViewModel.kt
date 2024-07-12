package com.hong7.coinnews.ui.feature.newsdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hong7.coinnews.data.repository.NewsRepository
import com.hong7.coinnews.model.Article
import com.hong7.coinnews.model.ArticleWithInterest
import com.hong7.coinnews.ui.NewsDetailNav
import com.hong7.coinnews.utils.GsonUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsDetailViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val articleJson = savedStateHandle.getStateFlow(ARTICLE_KEY, "")

    val article: StateFlow<Article?> = articleJson.map { GsonUtils.fromJson<Article>(it) }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(3_000),
            null
        )

    private val _isInterested = MutableStateFlow(false)
    val isInterested: StateFlow<Boolean> = _isInterested.asStateFlow()

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

private const val ARTICLE_KEY = "article"
