package com.hong7.coinnews.ui.feature.mycoinnews

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.crashlytics.crashlytics
import com.hong7.coinnews.data.repository.FilterRepository
import com.hong7.coinnews.data.repository.NewsRepository
import com.hong7.coinnews.model.Coin
import com.hong7.coinnews.model.Filter
import com.hong7.coinnews.model.News
import com.hong7.coinnews.model.exception.ResponseResource
import com.hong7.coinnews.model.exception.UnknownException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class MyCoinNewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    private val filterRepository: FilterRepository,
) : ViewModel() {

    val selectedCoin = MutableStateFlow<Coin?>(null)

    val uiState: StateFlow<MyCoinNewsUiState> = combine(
        filterRepository.getUserFilter(),
        selectedCoin,
        ::Pair
    ).flatMapLatest { pair ->
        val filter = pair.first
            ?: return@flatMapLatest flowOf(MyCoinNewsUiState.FilterEmpty)
        val selectedCoin = pair.second
            ?: return@flatMapLatest flowOf(MyCoinNewsUiState.Success(emptyList(), filter))

        newsRepository.getRecentNewsByCoin(selectedCoin)
            .flatMapLatest {
                when(it){
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
    }.catch { throwable ->
        Firebase.crashlytics.recordException(throwable)
        emit(MyCoinNewsUiState.Failed(UnknownException()))
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
        val newsList: List<News>,
        val filter: Filter
    ) : MyCoinNewsUiState

    data class Failed(
        val throwable: Throwable
    ) : MyCoinNewsUiState
}






