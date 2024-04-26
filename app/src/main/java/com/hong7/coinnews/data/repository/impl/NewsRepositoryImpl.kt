package com.hong7.coinnews.data.repository.impl

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.google.firebase.perf.FirebasePerformance
import com.google.firebase.perf.metrics.Trace
import com.hong7.coinnews.data.mapper.toDomain
import com.hong7.coinnews.network.retrofit.NaverService
import com.hong7.coinnews.data.paging.NewsArticlePagingSource
import com.hong7.coinnews.data.paging.GlobalArticlePagingSource
import com.hong7.coinnews.data.repository.NewsRepository
import com.hong7.coinnews.database.NewsEntity
import com.hong7.coinnews.database.UserNewsDao
import com.hong7.coinnews.model.Article
import com.hong7.coinnews.model.Coin
import com.hong7.coinnews.network.retrofit.CryptoNewsService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepositoryImpl @Inject constructor(
    private val naverService: NaverService,
    private val cryptoNewsService: CryptoNewsService,
    private val userNewsDao: UserNewsDao
) : NewsRepository {

    override fun getArticles(
        coin: Coin
    ): Flow<PagingData<Article>> {

        val searchWordsWithPlus = coin.relatedSearchWord.joinToString(separator = " | ")
        val query = "${coin.name} | ${searchWordsWithPlus}"
        Log.e("searchWordsWithPlus",coin.relatedSearchWord.toString())
        Log.e("searchWordsWithPlus",query)
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = 10),
            pagingSourceFactory = {
                NewsArticlePagingSource(
                    naverService,
                    query
                )
            }
        ).flow.map {
            it.map { it.toDomain() }
            val trace: Trace =
                FirebasePerformance.getInstance().newTrace("network_article_mapping")
            trace.start()
            it.map { it.toDomain() }

        }.flowOn(Dispatchers.IO)
    }

    override fun getGlobalArticles(
        coin: Coin
    ): Flow<PagingData<Article>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = 5),
            pagingSourceFactory = {
                GlobalArticlePagingSource(cryptoNewsService, coin)
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

    override fun getScrapedNews(): Flow<List<Article>> {
        return userNewsDao.getAllNews()
            .map { it.map { it.toDomain() } }
    }

    override fun isNewsScraped(id: String): Flow<Boolean> {
        return userNewsDao.isInterested(id)
    }

    override suspend fun addNewsScraped(article: Article) {
        userNewsDao.insert(
            NewsEntity(
                newsId = article.id,
                title = article.title,
                description = article.description,
                url = article.url,
                author = article.author,
                createdAt = article.createdAt ?: LocalDateTime.now()
            )
        )
    }

    override suspend fun deleteNewsScraped(article: Article) {
        userNewsDao.delete(article.id)
    }

    companion object {
        private const val DEFAULT_CRYPTO_QUERY = "암호화폐"
    }
}