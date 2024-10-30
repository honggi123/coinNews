package com.hong7.coinnews.data.repository

import androidx.paging.PagingData
import com.hong7.coinnews.model.VideoItem
import kotlinx.coroutines.flow.Flow

interface VideoRepository {

    fun getVideos(playListId: String): Flow<PagingData<VideoItem>>
}