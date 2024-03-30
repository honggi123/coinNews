package com.example.coinnews.ui.articlelist.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coinnews.data.repository.CoinRepository
import com.example.coinnews.data.repository.NewsRepository
import com.example.coinnews.data.repository.UserRepository
import com.example.coinnews.model.Article
import com.example.coinnews.model.ArticleWithInterest
import com.example.coinnews.model.CoinFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticleDetailViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _isInterested = MutableStateFlow(false)
    val isInterested: StateFlow<Boolean> = _isInterested.asStateFlow()

    fun updateArticle(article: Article?){
        viewModelScope.launch {
            if (article != null) {
                userRepository.isNewsInterested(article.id).collectLatest {
                    _isInterested.value = it
                }
            }
        }
    }

    fun toggleInterest(articleWithInterest: ArticleWithInterest) {
        viewModelScope.launch {
            if (articleWithInterest.isInterested) {
                userRepository.deleteNewsInterest(articleWithInterest.article)
            } else {
                userRepository.addNewsInterest(articleWithInterest.article)
            }
        }
    }
}