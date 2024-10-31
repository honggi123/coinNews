package com.hong7.coinnews.ui.feature.home

import androidx.lifecycle.viewModelScope
import com.hong7.coinnews.data.repository.NewsRepository
import com.hong7.coinnews.model.News
import com.hong7.coinnews.model.exception.ResponseResource
import com.hong7.coinnews.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

private const val CRYPTO_SEARCH_KEYWORD =
    "코인 | 비트코인 | 암호화폐 | 이더리움"

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
) : BaseViewModel() {

    // TODO uistate
    val news: StateFlow<List<News>?> =
        newsRepository.getRecentNewsByQuery(CRYPTO_SEARCH_KEYWORD)
            .flatMapLatest {
                when (it) {
                    is ResponseResource.Success -> flowOf(it.data)
                    is ResponseResource.Error -> {
                        onErrorResonse(it)
                        flowOf(null)
                    }
                    is ResponseResource.Loading -> flowOf(emptyList())
                }
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                emptyList()
            )

}