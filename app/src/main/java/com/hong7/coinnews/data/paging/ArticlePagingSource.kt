package com.hong7.coinnews.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.firebase.Firebase
import com.google.firebase.perf.FirebasePerformance
import com.google.firebase.perf.metrics.Trace
import com.google.firebase.perf.performance
import com.hong7.coinnews.network.model.NetworkArticle
import com.hong7.coinnews.network.model.NetworkCoinListItem
import com.hong7.coinnews.network.retrofit.NaverService
import javax.inject.Inject


private const val INITIAL_PAGE = 1

class ArticlePagingSource @Inject constructor(
    private val service: NaverService,
    private val query: String
) : PagingSource<Int, NetworkArticle>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NetworkArticle> {
        return try {
            val page = params.key ?: INITIAL_PAGE
            val trace: Trace =
                FirebasePerformance.getInstance().newTrace("network_article_request")
            trace.start()
            val response = service.getArticles(
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

    override fun getRefreshKey(state: PagingState<Int, NetworkArticle>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }
}