package com.hong7.coinnews.ui.feature.newslist

import androidx.lifecycle.viewModelScope
import com.hong7.coinnews.data.repository.NewsRepository
import com.hong7.coinnews.model.News
import com.hong7.coinnews.model.exception.ResponseResource
import com.hong7.coinnews.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

private const val CRYPTO_SEARCH_KEYWORD =
    "가상자산 | 비트코인 | 암호화폐 | 이더리움 | 두나무 | 빗썸 | 바이낸스 | 솔라나 | 밈 코인"

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
) : BaseViewModel() {

    val uiState: StateFlow<NewsScreenUiState> =
        newsRepository.getRecentNewsByQuery(CRYPTO_SEARCH_KEYWORD, 20)
            .flatMapLatest {
                when (it) {
                    is ResponseResource.Success -> {
                        flowOf(
                            NewsScreenUiState.Success(
                                newsList = it.data,
                                isNewsLoading = false
                            )
                        )
                    }

                    is ResponseResource.Error -> {
                        onErrorResonse(it)
                        flowOf(NewsScreenUiState.Failed(it.exception)) // TODO
                    }

                    is ResponseResource.Loading -> {
                        flowOf(NewsScreenUiState.Success(isNewsLoading = true))
                    }
                }
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                NewsScreenUiState.Loading
            )

    private val _watchedNewsIds = MutableStateFlow<Set<String>>(emptySet())
    val watchedNewsIds: StateFlow<Set<String>> = _watchedNewsIds.asStateFlow()

    fun onNewsClick(newsId: String) {
        val watchedNewsIds = watchedNewsIds.value.toMutableSet()
        watchedNewsIds.add(newsId)
        _watchedNewsIds.value = watchedNewsIds
    }


}

sealed interface NewsScreenUiState {
    object Loading : NewsScreenUiState


    data class Success(
        val newsList: List<News>? = null,
        val isNewsLoading: Boolean
    ) : NewsScreenUiState

    data class Failed(
        val throwable: Throwable
    ) : NewsScreenUiState
}






