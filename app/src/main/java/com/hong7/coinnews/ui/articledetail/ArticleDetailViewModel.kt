package com.hong7.coinnews.ui.articledetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hong7.coinnews.data.repository.NewsRepository
import com.hong7.coinnews.model.Article
import com.hong7.coinnews.model.ArticleWithInterest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticleDetailViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {

    private val _isInterested = MutableStateFlow(false)
    val isInterested: StateFlow<Boolean> = _isInterested.asStateFlow()

    fun updateArticle(article: Article?){
        viewModelScope.launch {
            if (article != null) {
                newsRepository.isNewsScraped(article.id).collectLatest {
                    _isInterested.value = it
                }
            }
        }
    }

    fun toggleInterest(articleWithInterest: ArticleWithInterest) {
        viewModelScope.launch {
            if (articleWithInterest.isInterested) {
                newsRepository.deleteNewsScraped(articleWithInterest.article)
            } else {
                newsRepository.addNewsScraped(articleWithInterest.article)
            }
        }
    }
}