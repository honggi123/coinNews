package com.example.coinnews.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.coinnews.database.AppDatabase
import com.example.coinnews.database.CoinFilterEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CoinFilterDatabaseWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val database = AppDatabase.getInstance(applicationContext)
            val filters = listOf(
                CoinFilterEntity(coinName = "비트코인", symbol = "BTC"),
                CoinFilterEntity(coinName = "이더리움", symbol = "ETC"),
                CoinFilterEntity(coinName = "리플", symbol = "XRP"),
                CoinFilterEntity(coinName = "솔라나", symbol = "SOL")
            )
            database.coinFilterDao().insertAll(filters)

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