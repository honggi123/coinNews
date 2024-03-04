package com.example.coinnews.data.repository.impl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.coinnews.data.network.retrofit.NewsService
import com.example.coinnews.data.paging.NewsPagingSource
import com.example.coinnews.data.repository.NewsRepository
import com.example.coinnews.model.Article
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepositoryImpl @Inject constructor(
    private val newsService: NewsService
) : NewsRepository {

    override fun getArticles(): Flow<PagingData<Article>> {
        return Pager(
            config = PagingConfig(
                enablePlaceholders = false, // todo
                pageSize = 5
            ),
            pagingSourceFactory = { NewsPagingSource(service = newsService, contentType = "news") }
        ).flow.map { it.map { TODO() } }
    }

    override fun getVideos(): Flow<PagingData<Article>> {
        return Pager(
            config = PagingConfig(
                enablePlaceholders = false, // todo
                pageSize = 5
            ),
            pagingSourceFactory = { NewsPagingSource(service = newsService, contentType = "video") }
        ).flow.map { it.map { TODO() } }
    }
}