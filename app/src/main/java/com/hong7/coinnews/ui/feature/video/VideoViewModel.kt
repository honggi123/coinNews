package com.hong7.coinnews.ui.feature.video

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.hong7.coinnews.data.repository.FilterRepository
import com.hong7.coinnews.data.repository.NewsRepository
import com.hong7.coinnews.data.repository.VideoRepository
import com.hong7.coinnews.model.Coin
import com.hong7.coinnews.model.Filter
import com.hong7.coinnews.model.News
import com.hong7.coinnews.model.VideoItem
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
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

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class VideoViewModel @Inject constructor(
    private val videoRepository: VideoRepository,
    private val filterRepository: FilterRepository,
) : ViewModel() {

    val selectedCoin = MutableStateFlow<Coin?>(null)

    val pagingCryptoNews: Flow<PagingData<VideoItem>> = combine(
        filterRepository.getUserFilter(),
        selectedCoin,
        ::Pair
    ).flatMapLatest { pair ->
        val filter = pair.first ?: throw NullPointerException()
        val currentCoin = pair.second ?: filter.coins.first().also { firstCoin ->
            selectedCoin.value = firstCoin
        }
        videoRepository.getVideos(currentCoin.name)
            .cachedIn(viewModelScope)
    }

//    val uiState: StateFlow<VideoScreenUiState> = combine(
//        filterRepository.getUserFilter(),
//        selectedCoin,
//        ::Pair
//    ).flatMapLatest { pair ->
//        val filter = pair.first ?: throw NullPointerException()
//        val currentCoin = pair.second ?: filter.coins.first().also { firstCoin ->
//            selectedCoin.value = firstCoin
//        }
//
//        videoRepository.getVideos(currentCoin.name)
//            .flatMapLatest {
//                when (it) {
//                    is ResponseResource.Success -> {
//                        flowOf(VideoScreenUiState.Success(it.data, filter))
//                    }
//
//                    is ResponseResource.Error -> {
//                        flowOf(VideoScreenUiState.Failed(it.exception))
//                    }
//
//                    is ResponseResource.Loading -> {
//                        flowOf(VideoScreenUiState.Loading)
//                    }
//                }
//            }
//    }
//        .stateIn(
//            viewModelScope,
//            SharingStarted.WhileSubscribed(5_000),
//            VideoScreenUiState.Loading
//        )

    fun onCoinClick(coin: Coin) {
        viewModelScope.launch {
            selectedCoin.value = coin
        }
    }
}

sealed interface VideoScreenUiState {
    object Loading : VideoScreenUiState

    object FilterEmpty : VideoScreenUiState

    data class Success(
        val newsList: List<News>?,
        val filter: Filter
    ) : VideoScreenUiState

    data class Failed(
        val throwable: Throwable
    ) : VideoScreenUiState
}
