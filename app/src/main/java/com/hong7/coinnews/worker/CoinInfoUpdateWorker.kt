package com.hong7.coinnews.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
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
            val lastUpdateDate = inputData.getLong(KEY_LAST_UPDATE_DATE, 0)
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = lastUpdateDate

            val database = AppDatabase.getInstance(applicationContext)
            val filterDao = database.filterDao()

            val existingFilter = filterDao.getRecentFilter()
            if (existingFilter != null) {
                val updatedCoins = CoinDataSource().getUpdatedCoins(calendar)
                    .map { it.toEntity() }

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

    companion object {

        const val KEY_LAST_UPDATE_DATE = "KEY_LAST_UPDATE_DATE"
        const val KEY_UNIQUE_NAME = "KEY_UPDATE_WORK"

        fun enqueue(workManager: WorkManager, updateDate: Calendar): OneTimeWorkRequest {
            val workRequest = OneTimeWorkRequestBuilder<CoinInfoUpdateWorker>()
                .setInputData(workDataOf(KEY_LAST_UPDATE_DATE to updateDate.timeInMillis))
                .build()
            workManager
                .beginUniqueWork(KEY_UNIQUE_NAME, ExistingWorkPolicy.APPEND_OR_REPLACE, workRequest)
                .enqueue()
            return workRequest
        }
    }
}