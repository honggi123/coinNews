package com.example.coinnews.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.coinnews.data.mapper.toEntity
import com.example.coinnews.database.AppDatabase
import com.example.coinnews.database.CoinFilterEntity
import com.example.coinnews.model.CoinFilter
import com.example.coinnews.model.CountryScope
import com.example.coinnews.model.Filter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CoinFilterDatabaseWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val database = AppDatabase.getInstance(applicationContext)
            val filter = Filter(
                coinFilters = listOf(
                    CoinFilter(coinName = "비트코인", symbol = "BTC"),
                    CoinFilter(coinName = "이더리움", symbol = "ETC"),
                    CoinFilter(coinName = "리플", symbol = "XRP"),
                    CoinFilter(coinName = "솔라나", symbol = "SOL"),
                    CoinFilter(coinName = "도지코인", symbol = "DOGE")
                ),
                scope = CountryScope.Local
            )
            database.filterDao().insert(filter.toEntity())

            Result.success()
        } catch (ex: Exception) {
            Result.failure()
        }
    }

    companion object {
        private const val TAG = "CoinFilterDatabaseWorker"
        const val KEY_FILENAME = "COIN_FILTER_DATA_FILENAME"
    }
}