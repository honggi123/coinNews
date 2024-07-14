package com.hong7.coinnews.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.firebase.perf.FirebasePerformance
import com.google.firebase.perf.metrics.Trace
import com.hong7.coinnews.network.model.NetworkNews
import com.hong7.coinnews.network.okhttp.retrofit.NaverService
import javax.inject.Inject


private const val INITIAL_PAGE = 1

class NewsNewsPagingSource @Inject constructor(
    private val service: NaverService,
    private val query: String
) : PagingSource<Int, NetworkNews>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NetworkNews> {
        return try {
            val page = params.key ?: INITIAL_PAGE
            val trace: Trace =
                FirebasePerformance.getInstance().newTrace("network_news_request")
            trace.start()
            val response = service.getNewss(
                query = query,
                page = page,
                pageSize = params.loadSize
            )
            trace.stop()
            LoadResult.Page(
                data = response.items,
                prevKey = if (page == INITIAL_PAGE) null else page - 1,
                nextKey = if (response.items.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, NetworkNews>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }
}