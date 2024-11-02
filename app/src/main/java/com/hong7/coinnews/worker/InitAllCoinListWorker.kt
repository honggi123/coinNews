package com.hong7.coinnews.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest.MIN_BACKOFF_MILLIS
import androidx.work.WorkerParameters
import com.hong7.coinnews.data.mapper.toEntity
import com.hong7.coinnews.database.dao.CoinDao
import com.hong7.coinnews.network.retrofit.CoinMarketCapService
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.coroutineScope
import timber.log.Timber
import java.util.concurrent.TimeUnit

@HiltWorker
class InitAllCoinListWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val coinDao: CoinDao,
    private val coinMarketCapService: CoinMarketCapService
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = coroutineScope {
        try {
            val coinList = coinMarketCapService.fetchCoinList()
                .data
                .map { it.toEntity() }
            coinDao.insertAll(coinList)
            Result.success()
        } catch (ex: Exception) {
            Timber.tag("InitAllCoinListWorker").e(ex)
            // TODO add firbase log
            Result.retry()
        }
    }

    companion object {

        // TODO 실행 중복 막기
        fun enqueue(workManager: WorkManager): OneTimeWorkRequest {
            val workRequest = OneTimeWorkRequestBuilder<InitAllCoinListWorker>()
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(
                            NetworkType.CONNECTED
                        )
                        .build()
                )
                .setBackoffCriteria(
                    BackoffPolicy.EXPONENTIAL,  // 재시도 간격 10초 -> 20초 -> 30초 ...
                    MIN_BACKOFF_MILLIS, // 최소 10초
                    TimeUnit.MILLISECONDS
                )
                .build()
            workManager.enqueue(workRequest)
            return workRequest
        }
    }
}
