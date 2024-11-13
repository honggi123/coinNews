package com.hong7.coinnews.data.repository.impl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.hong7.coinnews.data.paging.NewsPagingSource
import com.hong7.coinnews.data.paging.VideoPagingSource
import com.hong7.coinnews.data.repository.VideoRepository
import com.hong7.coinnews.model.News
import com.hong7.coinnews.model.VideoItem
import com.hong7.coinnews.network.retrofit.YoutubeService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VideoRepositoryImpl @Inject constructor(
    val youtubeService: YoutubeService
) : VideoRepository {
    override fun getVideos(playListId: String): Flow<PagingData<VideoItem>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = 20),
            pagingSourceFactory = { VideoPagingSource(youtubeService, playListId) }
        ).flow
    }
}