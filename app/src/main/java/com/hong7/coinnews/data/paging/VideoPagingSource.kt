package com.hong7.coinnews.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.hong7.coinnews.data.mapper.toDomain
import com.hong7.coinnews.model.VideoItem
import com.hong7.coinnews.network.retrofit.YoutubeService


private const val STARTING_PAGE_TOKEN = ""

class VideoPagingSource(
    private val service: YoutubeService,
    private val playListId: String,
) : PagingSource<String, VideoItem>() {

    override suspend fun load(params: LoadParams<String>): LoadResult<String, VideoItem> {
        return try {
            val page = params.key ?: STARTING_PAGE_TOKEN
            val response =
                service.getYoutubeVideoItems(pageToken = page, playListId = playListId)
            val items = response.items.map { it.toDomain() }
            LoadResult.Page(
                data = items,
                prevKey = if (page == STARTING_PAGE_TOKEN) null else response.prevPageToken,
                nextKey = if (items.isEmpty()) null else response.nextPageToken,
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }


    override fun getRefreshKey(state: PagingState<String, VideoItem>): String? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }
}
