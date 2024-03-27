package com.example.coinnews.data.repository.impl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.coinnews.data.mapper.toDomain
import com.example.coinnews.data.mapper.toNetwork
import com.example.coinnews.network.retrofit.CoinService
import com.example.coinnews.network.retrofit.ArticleService
import com.example.coinnews.data.paging.CoinPagingSource
import com.example.coinnews.data.paging.ArticlePagingSource
import com.example.coinnews.data.repository.NewsRepository
import com.example.coinnews.model.Article
import com.example.coinnews.model.Coin
import com.example.coinnews.model.CoinFilter
import com.example.coinnews.model.Sort
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepositoryImpl @Inject constructor(
    private val articleService: ArticleService,
) : NewsRepository {
    override fun isInterested(id: String): Flow<Boolean> = flow {
        emit(false)
    }

    override fun getArticles(
        filter: CoinFilter
    ): Flow<PagingData<Article>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = 10),
            pagingSourceFactory = { ArticlePagingSource(articleService, filter.coinName) }
        ).flow.map { it.map { it.toDomain(filter)} }
    }

    override suspend fun addInterest(article: Article) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteInterest(article: Article) {
        TODO("Not yet implemented")
    }

    companion object {
        private const val DEFAULT_CRYPTO_QUERY = "암호화폐"
    }
}