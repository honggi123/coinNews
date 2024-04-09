package com.hong7.coinnews.data.repository.impl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.google.firebase.perf.FirebasePerformance
import com.google.firebase.perf.metrics.Trace
import com.hong7.coinnews.data.mapper.toDomain
import com.hong7.coinnews.network.retrofit.NaverService
import com.hong7.coinnews.data.paging.ArticlePagingSource
import com.hong7.coinnews.data.paging.GlobalArticlePagingSource
import com.hong7.coinnews.data.repository.NewsRepository
import com.hong7.coinnews.model.Article
import com.hong7.coinnews.model.CoinFilter
import com.hong7.coinnews.network.retrofit.CryptoNewsService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepositoryImpl @Inject constructor(
    private val naverService: NaverService,
    private val cryptoNewsService: CryptoNewsService
) : NewsRepository {

    override fun getArticles(
        filter: CoinFilter?
    ): Flow<PagingData<Article>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = 10),
            pagingSourceFactory = {
                ArticlePagingSource(
                    naverService,
                    filter?.coinName ?: DEFAULT_CRYPTO_QUERY
                )
            }
        ).flow.map {
            val trace: Trace =
                FirebasePerformance.getInstance().newTrace("network_article_mapping")
            trace.start()
            val items = it.map { it.toDomain() }
            trace.stop()
            items
        }.flowOn(Dispatchers.IO)
    }

    override fun getGlobalArticles(filter: CoinFilter): Flow<PagingData<Article>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = 5),
            pagingSourceFactory = {
                GlobalArticlePagingSource(cryptoNewsService, filter)
            }
        ).flow.map {
            val trace: Trace =
                FirebasePerformance.getInstance().newTrace("network_global_article_mapping")
            trace.start()
            val items = it.map { it.toDomain() }
            trace.stop()
            items
        }
    }


    companion object {
        private const val DEFAULT_CRYPTO_QUERY = "암호화폐"
    }
}