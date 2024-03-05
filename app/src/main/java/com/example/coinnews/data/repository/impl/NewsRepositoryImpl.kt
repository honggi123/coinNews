package com.example.coinnews.data.repository.impl

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.coinnews.data.mapper.toArticle
import com.example.coinnews.data.network.retrofit.NewsService
import com.example.coinnews.data.paging.NewsPagingSource
import com.example.coinnews.data.repository.NewsRepository
import com.example.coinnews.model.Article
import com.example.coinnews.model.ArticleMetaData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private const val DEFAULT_CRYPTO_QUERY = "암호화폐"

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
            pagingSourceFactory = {
                NewsPagingSource(
                    service = newsService,
                    query = DEFAULT_CRYPTO_QUERY
                )
            }
        ).flow.map { it.map { it.toArticle() } }
    }

    override fun getVideos(): Flow<PagingData<Article>> {
        return Pager(
            config = PagingConfig(
                enablePlaceholders = false, // todo
                pageSize = 5
            ),
            pagingSourceFactory = { NewsPagingSource(service = newsService, query = DEFAULT_CRYPTO_QUERY) }
        ).flow.map { it.map { TODO() } }
    }
}