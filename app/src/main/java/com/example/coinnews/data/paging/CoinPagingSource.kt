package com.example.coinnews.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.coinnews.network.model.NetworkCoinListItem
import com.example.coinnews.network.retrofit.CoinService
import javax.inject.Inject

private const val INITIAL_PAGE = 1

class CoinPagingSource @Inject constructor(
    private val sortOption: String,
    private val sort: String,
    private val service: CoinService,
) : PagingSource<Int, NetworkCoinListItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NetworkCoinListItem> {
        return try {
            val page = params.key ?: INITIAL_PAGE

            val response = service.getCoins(
                page = page,
                pageSize = params.loadSize,
                sortOption = sortOption,
                sort = sort
            )
            LoadResult.Page(
                data = response.items,
                prevKey = if (page == INITIAL_PAGE) null else page - 1,
                nextKey = if (response.items.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    // TODO
    override fun getRefreshKey(state: PagingState<Int, NetworkCoinListItem>): Int? {
        return state.anchorPosition
    }
}