package com.example.coinnews.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.coinnews.data.network.model.NetworkArticle
import com.example.coinnews.data.network.retrofit.NewsService
import javax.inject.Inject


private const val INITIAL_PAGE = 1

class NewsPagingSource @Inject constructor(
    private val service: NewsService,
    private val query: String
) : PagingSource<Int, NetworkArticle>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NetworkArticle> {
        return try {
            val page = params.key ?: INITIAL_PAGE

            val response = service.getArticles(
                query = query,
                page = page,
                pageSize = params.loadSize
            )
            LoadResult.Page(
                data = response.items,
                prevKey = if (page == INITIAL_PAGE) {
                    null
                } else {
                    page - 1
                },
                nextKey = if (response.items.isEmpty()) {
                    null
                } else {
                    page + 1
                }
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    // TODO
    override fun getRefreshKey(state: PagingState<Int, NetworkArticle>): Int? {
        return state.anchorPosition
    }
}