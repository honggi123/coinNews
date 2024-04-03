package com.example.coinnews.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.coinnews.model.CoinFilter
import com.example.coinnews.network.model.NetworkArticle
import com.example.coinnews.network.model.NetworkGlobalNews
import com.example.coinnews.network.retrofit.CryptoNewsService
import com.example.coinnews.network.retrofit.NaverService
import javax.inject.Inject

private const val INITIAL_PAGE = 1

class GlobalArticlePagingSource @Inject constructor(
    private val service: CryptoNewsService,
    private val coinFilter: CoinFilter
) : PagingSource<Int, NetworkGlobalNews>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NetworkGlobalNews> {
        return try {
            val page = params.key ?: INITIAL_PAGE

            val response = service.getCoinNews(
                tickers = coinFilter.symbol,
                page = page,
                pageSize = 5
            )
            LoadResult.Page(
                data = response.items,
                prevKey = if (page == INITIAL_PAGE) null  else page - 1,
                nextKey = if (response.items.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    // TODO
    override fun getRefreshKey(state: PagingState<Int, NetworkGlobalNews>): Int? {
        return state.anchorPosition
    }
}