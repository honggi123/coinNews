package com.hong7.coinnews.data.repository

import androidx.paging.PagingData
import com.hong7.coinnews.model.News
import com.hong7.coinnews.model.Coin
import com.hong7.coinnews.model.exception.ResponseResource
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    fun getNews(query: String): Flow<PagingData<News>>

    fun getRecentNewsByQuery(query: String): Flow<ResponseResource<List<News>>>

}