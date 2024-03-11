package com.example.coinnews.data.repository

import androidx.paging.PagingData
import com.example.coinnews.model.Article
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    fun getArticles(): Flow<PagingData<Article>>
}