package com.hong7.coinnews.ui.feature.deprecated.scrap

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hong7.coinnews.data.repository.NewsRepository
import com.hong7.coinnews.model.News
import com.hong7.coinnews.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ScrapNewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : BaseViewModel() {

    val uiState: StateFlow<ScrapNewsUiState> = newsRepository.getScrapedNewsList()
        .map {
            ScrapNewsUiState.Success(it)
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            ScrapNewsUiState.Loading
        )
}

sealed interface ScrapNewsUiState {
    object Loading : ScrapNewsUiState

    data class Success(
        val newsList: List<News>
    ) : ScrapNewsUiState

    data class Failed(
        val throwable: Throwable
    ) : ScrapNewsUiState
}
