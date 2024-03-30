package com.example.coinnews.data.repository

import androidx.paging.PagingData
import com.example.coinnews.model.Article
import com.example.coinnews.model.Coin
import com.example.coinnews.model.Sort
import kotlinx.coroutines.flow.Flow

interface CoinRepository {

    fun getCoinInfo(
        coin: Coin,
    ): Flow<Coin?>
}