package com.hong7.coinnews.service.monitor

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.google.auth.oauth2.GoogleCredentials
import com.google.auth.oauth2.IdTokenCredentials
import com.google.auth.oauth2.IdTokenProvider
import com.google.auth.oauth2.ImpersonatedCredentials
import com.hong7.coinnews.R
import com.hong7.coinnews.data.extensions.asResponseResourceFlow
import com.hong7.coinnews.data.extensions.runAsResponseResource
import com.hong7.coinnews.database.dao.CoinDao
import com.hong7.coinnews.database.entity.CoinEntity
import com.hong7.coinnews.model.Coin
import com.hong7.coinnews.model.exception.BadRequestException
import com.hong7.coinnews.model.exception.ConflictException
import com.hong7.coinnews.model.exception.ForbiddenException
import com.hong7.coinnews.model.exception.InternetServerException
import com.hong7.coinnews.model.exception.NetworkNotConnectedException
import com.hong7.coinnews.model.exception.NotFoundException
import com.hong7.coinnews.model.exception.ResponseResource
import com.hong7.coinnews.network.model.request.SendNotificationRequest
import com.hong7.coinnews.network.model.response.DaysCandleResponse
import com.hong7.coinnews.network.model.response.MinutesCandleResponse
import com.hong7.coinnews.network.retrofit.GoogleCloudService
import com.hong7.coinnews.network.retrofit.UpbitService
import com.hong7.coinnews.preference.PreferenceManager
import com.hong7.coinnews.ui.main.MainActivity
import com.hong7.coinnews.utils.PriceUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.FileInputStream
import java.io.IOException
import java.lang.Math.sqrt
import java.time.Duration
import java.time.LocalDateTime
import java.util.Arrays
import javax.inject.Inject


@AndroidEntryPoint
class CoinMonitorForegroundService : Service() {

    @Inject
    lateinit var upbitService: UpbitService

    @Inject
    lateinit var dao: CoinDao

    @Inject
    lateinit var googleCloudService: GoogleCloudService

    @Inject
    lateinit var preferenceManager: PreferenceManager

    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    private val lastPriceAlertTimes = mutableMapOf<CoinEntity, LocalDateTime>()
    private val lastVolumeAlertTimes = mutableMapOf<CoinEntity, LocalDateTime>()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotification()
        coroutineScope.launch {
            preferenceManager.getFcmToken().collectLatest { token ->
                startVolumeMonitor(token)
                startPricePercentageMonitor(token)
            }
        }

        return START_NOT_STICKY
    }

    private fun startVolumeMonitor(token: String) {
        coroutineScope.launch {
            combine(
                preferenceManager.getCoinVolumeAlertEnabled(),
                preferenceManager.getCoinVolumeAlertRatio()
            ) { enabled, ratio ->
                if (ratio == null) {
                    throw NullPointerException("알림 비율이 설정되지 않았습니다.")
                }
                Pair(enabled, ratio)
            }.collectLatest { (enabled, ratio) ->
                dao.getCoins().collectLatest { list ->
                    while (enabled) {
                        list.map { coin ->
                            delay(1000)
                            val lastAlertTime =
                                lastVolumeAlertTimes.getOrDefault(coin, null)
                            if (lastAlertTime != null) {
                                if (Duration.between(
                                        lastAlertTime,
                                        LocalDateTime.now()
                                    ).toHours() < 1
                                ) {
                                    return@map
                                }
                            }

                            val response = runAsResponseResource {
                                upbitService.fetchLatestCandles(market = coin.marketId)
                            }

                            when (response) {
                                is ResponseResource.Success -> {
                                    calculateVolumeChange(
                                        response.data,
                                        ratio,
                                        coin,
                                        token
                                    )
                                }

                                is ResponseResource.Error -> {
                                    hanldeApiError(
                                        retryBlock = {
                                            val response =
                                                upbitService.fetchLatestCandles(market = coin.marketId)
                                            calculateVolumeChange(
                                                response,
                                                ratio,
                                                coin,
                                                token
                                            )
                                        },
                                        result = response
                                    )
                                }

                                is ResponseResource.Loading -> {

                                }
                            }
                        }
                    }
                    delay(1_000L)
                }
            }
        }
    }

    private fun startPricePercentageMonitor(token: String) {
        coroutineScope.launch {
            combine(
                preferenceManager.getCoinPriceChangeAlertEnabled(),
                preferenceManager.getCoinPriceChangeAlertRatio()
            ) { enabled, ratio ->
                if (ratio == null) {
                    throw NullPointerException("알림 비율이 설정되지 않았습니다.")
                }
                Pair(enabled, ratio)
            }.collectLatest { (enabled, ratio) ->
                dao.getCoins().collect { list ->
                    while (enabled) {
                        list.map { coin ->
                            delay(1000)
                            val lastAlertTime =
                                lastPriceAlertTimes.getOrDefault(coin, null)
                            if (lastAlertTime != null) {
                                if (Duration.between(
                                        lastAlertTime,
                                        LocalDateTime.now()
                                    ).toMinutes() < 30
                                ) {
                                    return@map
                                }
                            }
                            val response =
                                runAsResponseResource {
                                    upbitService.fetchDaysCandles(market = coin.marketId)
                                }

                            when (response) {
                                is ResponseResource.Success -> {
                                    calculateChangeRate(
                                        response.data,
                                        ratio,
                                        coin,
                                        token
                                    )
                                }

                                is ResponseResource.Error -> {
                                    hanldeApiError(
                                        retryBlock = {
                                            val response =
                                                upbitService.fetchDaysCandles(market = coin.marketId)
                                            calculateChangeRate(
                                                response,
                                                ratio,
                                                coin,
                                                token
                                            )
                                        },
                                        result = response
                                    )
                                }

                                is ResponseResource.Loading -> {

                                }
                            }
                        }
                    }
                    delay(10_000L)
                }
            }
        }
    }

    private suspend fun hanldeApiError(
        retryBlock: suspend () -> Unit,
        result: ResponseResource.Error
    ) {
        when (result.exception) {
            is InternetServerException -> {
                retry(
                    delayMillis = 60000L,
                    block = retryBlock,
                    onFailure = {
                        Handler(Looper.getMainLooper()).post {
                            Toast.makeText(
                                this,
                                "서버에 문제가 발생했습니다. 잠시 후 다시 모니터링을 실행해주세요.",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        this.stopSelf()
                    })
            }

            is NetworkNotConnectedException -> {
                retry(
                    delayMillis = 3000L,
                    block = retryBlock,
                    onFailure = {
                        Handler(Looper.getMainLooper()).post {
                            Toast.makeText(this, "모니터링에 실패했습니다. 인터넷 연결 상태를 확인해주세요.", Toast.LENGTH_LONG)
                                .show()
                        }
                        this.stopSelf()
                    }
                )
            }

            else -> {
                Timber.tag("uncaughtNetworkException").e(result.exception)
                throw result.exception
            }
        }
    }

    private suspend fun calculateVolumeChange(
        response: List<MinutesCandleResponse>,
        ratio: Int,
        coin: CoinEntity,
        fcmToken: String
    ) {
        val volumes = response.map { it.candleAccTradeVolume }
        val firstVolume = volumes.first()
        val threshold = volumes.drop(1).average() * (ratio.div(100))

        if (firstVolume >= threshold) {
            // 거래량 급등
            Timber.i("${coin.koreanName} ${ratio} 거래량 급등 포착!!!")

            // TODO accessToken null 처리

//                                val accessToken = getAccessToken(
//                                    "coin-news-418815@appspot.gserviceaccount.com",
//                                    "https://www.googleapis.com/auth/cloud-platform"
//                                )

            val response = googleCloudService.sendPushNotificaiton(
                request = SendNotificationRequest(
                    "❗ ${coin.koreanName} 거래량 급등 ❗",
                    "${ratio}% 이상의 거래량 변동성 포착!!",
                    fcmToken
                )
            )
            if (response.success) {
                lastVolumeAlertTimes.put(coin, LocalDateTime.now())
            }

        } else {
            Timber.i("거래량 변동성 ${ratio} 없음 : ${coin.koreanName}")
        }
    }

    private suspend fun calculateChangeRate(
        response: List<DaysCandleResponse>,
        ratio: Int,
        coin: CoinEntity,
        fcmToken: String
    ) {
        val todayChangeRate =
            response.map { it.changeRate }.first()

        if (todayChangeRate >= (ratio.div(100.0))
        ) {
            // 가격 급등
            Timber.i("${coin.koreanName} 가격 변동성 포착!!!")

//                                val accessToken = getAccessToken(
//                                    "coinalert-gcp@coin-news-418815.iam.gserviceaccount.com\n",
//                                    "https://www.googleapis.com/auth/cloud-platform"
//                                )
            val response = googleCloudService.sendPushNotificaiton(
                request = SendNotificationRequest(
                    "❗ ${coin.koreanName} 가격 급등 ❗",
                    "전일 대비 ${
                        String.format(
                            "%.1f",
                            todayChangeRate * 100
                        )
                    }% 포착",
                    fcmToken
                )
            )

            if (response.success) {
                lastPriceAlertTimes.put(coin, LocalDateTime.now())
            }
        } else {
            Timber.i("가격 ${ratio} 변동성 없음 : ${coin.koreanName}")
        }
    }

    fun getAccessToken(impersonatedServiceAccount: String, scope: String): String {

        // Construct the GoogleCredentials object which obtains the default configuration from your
        // working environment.

        val googleCredentials =
            GoogleCredentials.fromStream(
                resources.assets.open("coin-news-418815-5bf639d9a4bf.json")
            )

        // delegates: The chained list of delegates required to grant the final accessToken.
        // For more information, see:
        // https://cloud.google.com/iam/docs/create-short-lived-credentials-direct#sa-credentials-permissions
        // Delegate is NOT USED here.
        val delegates: List<String>? = null

        // Create the impersonated credential.
        val impersonatedCredentials: ImpersonatedCredentials =
            ImpersonatedCredentials.newBuilder()
                .setSourceCredentials(googleCredentials)
                .setTargetPrincipal(impersonatedServiceAccount)
                .setScopes(Arrays.asList(scope))
                .setDelegates(delegates)
                .build()
        // Get the OAuth2 token.
        // Once you've obtained the OAuth2 token, you can use it to make an authenticated call.
        impersonatedCredentials.refresh();
        return impersonatedCredentials.accessToken.tokenValue
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    private fun createNotification() {
        val builder = NotificationCompat.Builder(this, "default")
        builder.setSmallIcon(R.mipmap.ic_launcher)
        builder.setContentTitle("시세 포착 봇 작동중")
        builder.color = Color.RED
        val notificationIntent = Intent(this, MainActivity::class.java)
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, notificationIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        builder.setContentIntent(pendingIntent) // 알림 클릭 시 이동

        // 알림 표시
        val notificationManager =
            this.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(
            NotificationChannel(
                "default",
                "기본 채널",
                NotificationManager.IMPORTANCE_DEFAULT
            )
        )

        notificationManager.notify(1, builder.build())
        val notification = builder.build()
        startForeground(1, notification)
        notificationManager.notify(1, notification)
    }

    private suspend fun retry(
        maxRetries: Int = 3,
        delayMillis: Long = 1000L,
        block: suspend () -> Unit,
        onFailure: suspend () -> Unit
    ) {
        var attempt = 0

        while (attempt <= maxRetries) {
            CoroutineScope(Dispatchers.IO + SupervisorJob())
                .launch {
                    block()
                }.runCatching {
                    attempt++
                    if (attempt >= maxRetries) {
                        onFailure()
                    }
                }
            delay(delayMillis)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
    }
}