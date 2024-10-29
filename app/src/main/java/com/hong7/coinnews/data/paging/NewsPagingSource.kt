package com.hong7.coinnews.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.hong7.coinnews.data.mapper.toDomain
import com.hong7.coinnews.model.News
import com.hong7.coinnews.network.retrofit.NaverService
import javax.inject.Inject


private const val INITIAL_PAGE = 1

class NewsPagingSource @Inject constructor(
    private val service: NaverService,
    private val query: String
) : PagingSource<Int, News>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, News> {
        return try {
            val page = params.key ?: INITIAL_PAGE
            val start = ((page - 1) * 30) + 1

            val newsItems = service.fetchNews(
                query = query,
                start = start,
                pageSize = 30
            )
                .items
                .map { it.toDomain() }
            LoadResult.Page(
                data = newsItems,
                prevKey = if (page == INITIAL_PAGE) null else page - 1,
                nextKey = if (newsItems.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, News>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }
}