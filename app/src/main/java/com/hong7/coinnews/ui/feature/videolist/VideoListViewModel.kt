package com.hong7.coinnews.ui.feature.videolist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.hong7.coinnews.data.repository.VideoRepository
import com.hong7.coinnews.model.Influencer
import com.hong7.coinnews.model.News
import com.hong7.coinnews.model.VideoItem
import com.hong7.coinnews.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
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

    // TODO
    val mockInfluencerList = listOf(
        Influencer(
            "UUa0nvMcv3bQzxFTjSyp_Vvg",
            "매억남",
            "https://yt3.ggpht.com/ytc/AIdro_lxW1wtZfz1yYRe0RevVhGyVeAjcSDSZco4x3KwqYGJjBg=s800-c-k-c0x00ffffff-no-rj"
        ),
        Influencer(
            "UUWZ6ZwIlzPydIGEi1XW4nwQ",
            "찰리브라웅",
            "https://yt3.ggpht.com/h31U1--WROqidKks0i_2E6ZDz7rS49msCrtrqT8S7wmyDKouO5fFZOf2w7XkO_U1HGtEsEQxNQ=s800-c-k-c0x00ffffff-no-rj"
        ),
        Influencer(
            "UURAAOkMnOxEPfy7wA98iVBg",
            "더픽",
            "https://yt3.ggpht.com/IoX6rmD1N7DqwxFhikplA3BxH8_5QgxzwGFwlRjk4XRZAUxooVUJX6c6c4Zn42LYcwrsP1mBitk=s800-c-k-c0x00ffffff-no-rj"
        ),
        Influencer(
            "UUuj_rpY3vgPHAwBrIGOLT5A",
            "블록미디어",
            "https://yt3.ggpht.com/ijTAIugFCjHhTuq7MPAzMHGiIZ-db9vszdr73Xq-W9BkfwyCDfaQgyqDTtuKDEMNjGCl8xzHBg=s800-c-k-c0x00ffffff-no-rj"
        ),
        Influencer(
            "UUvhsQm_E8wx2t5haDnCejMg",
            "코인이슈 경제채널",
            "https://yt3.ggpht.com/TODiJUH5GuD6xQgb3TBlYsXmvA92XlX4R1nVl-Ts0Vmel5lV10okXHXryJUfTvfuoxCLrUYd66w=s800-c-k-c0x00ffffff-no-rj"
        )
    )

    private val _selectedInfluencer = MutableStateFlow<Influencer?>(mockInfluencerList.first())
    val selectedInfluencer = _selectedInfluencer

    val pagingVideoItems: Flow<PagingData<VideoItem>> = selectedInfluencer
        .filterNotNull()
        .flatMapLatest {
            videoRepository.getVideos(it.id)
                .cachedIn(viewModelScope)
        }

    fun onInfluencerSelect(influencer: Influencer) {
        _selectedInfluencer.value = influencer
    }
}

sealed interface InfluencerDetailUiState {
    object Loading : InfluencerDetailUiState

    data class Success(
        val newsList: List<News>?,
    ) : InfluencerDetailUiState

    data class Failed(
        val throwable: Throwable
    ) : InfluencerDetailUiState
}
