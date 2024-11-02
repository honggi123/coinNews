package com.hong7.coinnews.ui.feature.videolist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.hong7.coinnews.data.repository.VideoRepository
import com.hong7.coinnews.model.News
import com.hong7.coinnews.model.VideoItem
import com.hong7.coinnews.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import javax.inject.Inject

@HiltViewModel
class VideoListViewModel @Inject constructor(
    private val videoRepository: VideoRepository,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val influencerId = savedStateHandle.getStateFlow("influencerId", "")
        .map {
            URLDecoder.decode(it, StandardCharsets.UTF_8.toString())
        }   // TODO refactor

    val pagingVideoItems: Flow<PagingData<VideoItem>> = influencerId.flatMapLatest {
        videoRepository.getVideos(it)
            .cachedIn(viewModelScope)
    }
}

sealed interface InfluencerDetailUiState {
    object Loading : InfluencerDetailUiState

    object FilterEmpty : InfluencerDetailUiState

    data class Success(
        val newsList: List<News>?,
    ) : InfluencerDetailUiState

    data class Failed(
        val throwable: Throwable
    ) : InfluencerDetailUiState
}
