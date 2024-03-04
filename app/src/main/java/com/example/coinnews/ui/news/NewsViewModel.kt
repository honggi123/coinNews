package com.example.coinnews.ui.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.coinnews.data.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

// todo uistate
@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {

    val articles = newsRepository.getArticles()
        .cachedIn(viewModelScope)

    val videos = newsRepository.getVideos()
        .cachedIn(viewModelScope)
}
