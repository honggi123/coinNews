package com.hong7.coinnews.ui.feature.influencer_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.hong7.coinnews.data.repository.FilterRepository
import com.hong7.coinnews.data.repository.VideoRepository
import com.hong7.coinnews.model.Coin
import com.hong7.coinnews.model.Filter
import com.hong7.coinnews.model.News
import com.hong7.coinnews.model.VideoItem
import com.hong7.coinnews.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import javax.inject.Inject

@HiltViewModel
class InfluencerDetailViewModel @Inject constructor(
    private val videoRepository: VideoRepository,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val influencerId = savedStateHandle.getStateFlow("influencerId", "")
        .map {
            URLDecoder.decode(it, StandardCharsets.UTF_8.toString())
        }

    val pagingVideoItems: Flow<PagingData<VideoItem>> = influencerId.flatMapLatest {
        videoRepository.getVideos(it)
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

}

sealed interface InfluencerDetailUiState {
    object Loading : InfluencerDetailUiState

    object FilterEmpty : InfluencerDetailUiState

    data class Success(
        val newsList: List<News>?,
        val filter: Filter
    ) : InfluencerDetailUiState

    data class Failed(
        val throwable: Throwable
    ) : InfluencerDetailUiState
}
