package com.hong7.coinnews.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.firebase.perf.FirebasePerformance
import com.google.firebase.perf.metrics.Trace
import com.hong7.coinnews.model.CoinFilter
import com.hong7.coinnews.network.model.NetworkGlobalNews
import com.hong7.coinnews.network.retrofit.CryptoNewsService
import javax.inject.Inject

private const val INITIAL_PAGE = 1

class GlobalArticlePagingSource @Inject constructor(
    private val service: CryptoNewsService,
    private val coinFilter: CoinFilter
) : PagingSource<Int, NetworkGlobalNews>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NetworkGlobalNews> {
        return try {
            val page = params.key ?: INITIAL_PAGE
            val trace: Trace =
                FirebasePerformance.getInstance().newTrace("network_global_article_request")
            trace.start()
            val response = service.getCoinNews(
                tickers = coinFilter.symbol,
                page = page,
                pageSize = params.loadSize
            )
            trace.stop()
            LoadResult.Page(
                data = response.items,
                prevKey = if (page == INITIAL_PAGE) null  else page - 1,
                nextKey = if (response.items.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, NetworkGlobalNews>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }
}