package com.hong7.coinnews.ui.feature.mycoinnews

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.crashlytics.crashlytics
import com.hong7.coinnews.data.repository.FilterRepository
import com.hong7.coinnews.data.repository.NewsRepository
import com.hong7.coinnews.model.News
import com.hong7.coinnews.model.Coin
import com.hong7.coinnews.model.Filter
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
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class MyCoinNewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    private val filterRepository: FilterRepository,
) : ViewModel() {

    private val selectedCoin = MutableStateFlow<Coin?>(null)

    val uiState: StateFlow<MyCoinNewsUiState> = combine(
        filterRepository.getUserFilter(),
        selectedCoin,
        ::Pair
    ).flatMapLatest { pair ->
        val filter = pair.first
        val selectedCoin = pair.second ?: filter?.coins?.firstOrNull()
        if (filter != null) {
            newsRepository.getRecentNewsByCoin(selectedCoin!!) // todo
                .flatMapLatest { news ->
                    flowOf(MyCoinNewsUiState.Success(news, selectedCoin, filter))
                }
        } else {
            flowOf(MyCoinNewsUiState.FilterEmpty)
        }
    }.catch {
        Firebase.crashlytics.recordException(it)
        emit(MyCoinNewsUiState.Failed(it))
    }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            MyCoinNewsUiState.Loading
        )

    private val _watchedNewsIds = MutableStateFlow<Set<String>>(emptySet())
    val watchedNewsIds: StateFlow<Set<String>> = _watchedNewsIds.asStateFlow()

    fun onNewsClick(newsId: String) {
        val watchedNewsIds = _watchedNewsIds.value.toMutableSet()
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
        val selectedCoin: Coin,
        val filter: Filter
    ) : MyCoinNewsUiState

    data class Failed(
        val throwable: Throwable
    ) : MyCoinNewsUiState
}






