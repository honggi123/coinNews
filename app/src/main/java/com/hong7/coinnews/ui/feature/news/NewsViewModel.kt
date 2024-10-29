package com.hong7.coinnews.ui.feature.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.hong7.coinnews.data.repository.FilterRepository
import com.hong7.coinnews.data.repository.NewsRepository
import com.hong7.coinnews.model.Coin
import com.hong7.coinnews.model.Filter
import com.hong7.coinnews.model.News
import com.hong7.coinnews.model.exception.ResponseResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val CRYPTO_SEARCH_KEYWORD = "crypto | bitcoin | cryptocurrency | ethereum | 코인 | 비트코인 | 암호화폐 | 이더리움"

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    private val filterRepository: FilterRepository,
) : ViewModel() {

    val selectedCoin = MutableStateFlow<Coin?>(null)

    // TODO
    val pagingCryptoNews: Flow<PagingData<News>>
        get() = newsRepository.getNews(CRYPTO_SEARCH_KEYWORD)
            .cachedIn(viewModelScope)

    val uiState: StateFlow<MyCoinNewsUiState> = combine(
        filterRepository.getUserFilter(),
        selectedCoin,
        ::Pair
    ).flatMapLatest { pair ->
        val filter = pair.first
            ?: return@flatMapLatest flowOf(MyCoinNewsUiState.FilterEmpty)
        val selectedCoin = pair.second
            ?: return@flatMapLatest flowOf(MyCoinNewsUiState.Success(null, filter))

        newsRepository.getRecentNewsByCoin(selectedCoin)
            .flatMapLatest {
                when (it) {
                    is ResponseResource.Success -> {
                        flowOf(MyCoinNewsUiState.Success(it.data, filter))
                    }

                    is ResponseResource.Error -> {
                        flowOf(MyCoinNewsUiState.Failed(it.exception))
                    }

                    is ResponseResource.Loading -> {
                        flowOf(MyCoinNewsUiState.Loading)
                    }
                }
            }
    }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            MyCoinNewsUiState.Loading
        )

    private val _watchedNewsIds = MutableStateFlow<Set<String>>(emptySet())
    val watchedNewsIds: StateFlow<Set<String>> = _watchedNewsIds.asStateFlow()

    fun onNewsClick(newsId: String) {
        val watchedNewsIds = watchedNewsIds.value.toMutableSet()
        watchedNewsIds.add(newsId)
        _watchedNewsIds.value = watchedNewsIds
    }

    fun onCoinClick(coin: Coin) {
        viewModelScope.launch {
            selectedCoin.value = coin
        }
    }
}

sealed interface MyCoinNewsUiState {
    object Loading : MyCoinNewsUiState

    object FilterEmpty : MyCoinNewsUiState

    data class Success(
        val newsList: List<News>?,
        val filter: Filter
    ) : MyCoinNewsUiState

    data class Failed(
        val throwable: Throwable
    ) : MyCoinNewsUiState
}






