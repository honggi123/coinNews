package com.hong7.coinnews.data.repository.impl

import android.util.Size
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.hong7.coinnews.data.extensions.asResponseResourceFlow
import com.hong7.coinnews.data.mapper.toDomain
import com.hong7.coinnews.data.paging.NewsPagingSource
import com.hong7.coinnews.data.repository.NewsRepository
import com.hong7.coinnews.model.News
import com.hong7.coinnews.model.exception.ResponseResource
import com.hong7.coinnews.network.retrofit.NaverService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepositoryImpl @Inject constructor(
    private val naverService: NaverService
) : NewsRepository {

    override fun getNews(query: String): Flow<PagingData<News>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = NETWORK_PAGE_SIZE),
            pagingSourceFactory = { NewsPagingSource(naverService, query) }
        ).flow
    }

    override fun getRecentNewsByQuery(query: String, size: Int): Flow<ResponseResource<List<News>>> = flow {
        val news = naverService.fetchNews(query = query, start = 1, pageSize = size).items
            .map { it.toDomain() }
        emit(news)
    }.asResponseResourceFlow()


    companion object {
        private const val NETWORK_PAGE_SIZE = 20
    }
}