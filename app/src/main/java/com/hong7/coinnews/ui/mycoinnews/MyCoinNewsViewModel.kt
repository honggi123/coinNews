package com.hong7.coinnews.ui.mycoinnews

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hong7.coinnews.data.repository.FilterRepository
import com.hong7.coinnews.data.repository.NewsRepository
import com.hong7.coinnews.model.Article
import com.hong7.coinnews.model.Coin
import com.hong7.coinnews.model.Filter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MyCoinNewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    private val filterRepository: FilterRepository,
) : ViewModel() {

    val selectedCoin = filterRepository.getFilterStream()
        .flatMapLatest { filter ->
            val selectedCoin = filter?.coins
                ?.filter { it.isSelected }
                ?.firstOrNull()
            flowOf(selectedCoin)
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(3_000),
            null
        )

    private val _watchedNewsIds = MutableStateFlow<Set<String>>(emptySet())
    val watchedNewsIds: StateFlow<Set<String>> = _watchedNewsIds.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<MyCoinNewsUiState> = filterRepository.getFilterStream()
        .flatMapLatest { filter ->
            val selectedCoin = filter?.coins
                ?.filter { it.isSelected }
                ?.firstOrNull()
            if (selectedCoin != null) {
                newsRepository.getRecentNewsByCoin(selectedCoin)
                    .flatMapLatest {
                        flowOf(MyCoinNewsUiState.Success(it, filter))
                    }
            } else {
                flowOf(MyCoinNewsUiState.SelectedCoinEmpty)
            }
        }
        .catch { TODO() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(3_000),
            MyCoinNewsUiState.Loading
        )

    fun onNewsClick(newsId: String) {
        val watchedNewsIds = _watchedNewsIds.value.toMutableSet()
        watchedNewsIds.add(newsId)
        _watchedNewsIds.value = watchedNewsIds
    }

    fun onCoinClick(coin: Coin) {
        viewModelScope.launch {
            filterRepository.saveSelectedCoin(coin)
        }
    }
}

sealed interface MyCoinNewsUiState {

    object Loading : MyCoinNewsUiState

    object SelectedCoinEmpty : MyCoinNewsUiState

    data class Success(
        val newsList: List<Article>,
        val filter: Filter
    ) : MyCoinNewsUiState

    data class Failed(
        val throwable: Throwable
    ) : MyCoinNewsUiState
}






