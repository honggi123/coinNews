package com.hong7.coinnews.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.hong7.coinnews.data.mapper.toEntity
import com.hong7.coinnews.database.AppDatabase
import com.hong7.coinnews.database.FilterEntity
import com.hong7.coinnews.network.firebase.CoinDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Calendar

class CoinInfoUpdateWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val database = AppDatabase.getInstance(applicationContext)
            val filterDao = database.filterDao()

            if (!filterDao.isEmpty()) {
                val updatedCoins = CoinDataSource().getUpdatedCoins(Calendar.getInstance())
                    .map { it.toEntity() }

                val existingFilter = filterDao.getRecentFilter()
                val existingCoins = existingFilter.coins

                val updatedCoinMap = updatedCoins.associateBy { it.id }

                val newCoins = existingCoins.map { coin ->
                    updatedCoinMap[coin.id] ?: coin
                }

                filterDao.insert(
                    FilterEntity(
                        id = existingFilter.id,
                        coins = newCoins
                    )
                )
            }

            Result.success()
        } catch (ex: Exception) {
            Result.failure()
        }
    }
}