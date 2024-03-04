package com.example.coinnews.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.coinnews.data.network.model.NetworkArticle
import com.example.coinnews.data.network.retrofit.NewsService
import javax.inject.Inject


private const val INITIAL_PAGE = 1

class NewsPagingSource @Inject constructor(
    private val contentType: String,
    private val service: NewsService
) : PagingSource<Int, NetworkArticle>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NetworkArticle> {
        return try {
            val page = params.key ?: INITIAL_PAGE

            val result = service.getArticles(
                page,
                params.loadSize,
                contentType
            )

            LoadResult.Page(
                data = result.data,
                prevKey = if (page == INITIAL_PAGE) {
                    null
                } else {
                    page - 1
                },
                nextKey = if (result.data.isEmpty()) {
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