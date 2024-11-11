package com.hong7.coinnews.service.monitor

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.google.auth.oauth2.GoogleCredentials
import com.google.auth.oauth2.IdTokenCredentials
import com.google.auth.oauth2.IdTokenProvider
import com.google.auth.oauth2.ImpersonatedCredentials
import com.hong7.coinnews.R
import com.hong7.coinnews.database.dao.CoinDao
import com.hong7.coinnews.network.model.request.SendNotificationRequest
import com.hong7.coinnews.network.retrofit.GoogleCloudService
import com.hong7.coinnews.network.retrofit.UpbitService
import com.hong7.coinnews.preference.PreferenceManager
import com.hong7.coinnews.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.FileInputStream
import java.io.IOException
import java.lang.Math.sqrt
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

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotification()
        GlobalScope.launch(Dispatchers.Default) {
            preferenceManager.getFcmToken().collectLatest { token ->
                // TODO token null
                if (token != null) {
                    startVolumeMonitor(token)
                    startPricePercentageMonitor(token)
                }
            }
        }

        return START_NOT_STICKY
    }

    private fun startVolumeMonitor(token: String) {
        GlobalScope.launch(Dispatchers.Default) {
            preferenceManager.getCoinVolumeAlertEnabled().collectLatest { enabled ->
                if (enabled) {
                    while (true) {
                        dao.getCoins().collect { list ->
                            list.map { coin ->
                                if (coin.marketId.equals("KRW-UPP")) {
                                    return@map
                                }
                                delay(1000)

                                val response =
                                    upbitService.fetchLatestCandles(market = coin.marketId)
                                val volumes = response.map { it.candleAccTradeVolume }

                                // 표준 편차
                                val mean = volumes.average()
                                val variance = volumes.map { (it - mean) * (it - mean) }.average()
                                val standardDeviation = sqrt(variance)

                                val threshold = mean + 2 * standardDeviation
                                if (volumes.last() > threshold) {
                                    // 거래량 급등
                                    Timber.i("${coin.koreanName} 거래량 급등 포착!!!")

                                    // TODO accessToken null 처리

                                    val accessToken = getAccessToken(
                                        "coin-news-418815@appspot.gserviceaccount.com",
                                        "https://www.googleapis.com/auth/cloud-platform"
                                    )
                                    googleCloudService.sendPushNotificaiton(
                                        authorization = "bearer ${accessToken}",
                                        request = SendNotificationRequest(
                                            "종목명 : ${coin.koreanName} 거래량 급등",
                                            "거래량 변동성이 포착되었습니다!!",
                                            token
                                        )
                                    )
                                } else {
                                    Timber.i("거래량 변동성 없음 : ${coin.koreanName}")
                                }
                            }
                        }
                        delay(1_000L)
                    }
                }
            }
        }
    }

    private fun startPricePercentageMonitor(token: String) {
        GlobalScope.launch(Dispatchers.Default) {
            preferenceManager.getCoinPriceChangeAlertEnabled().collectLatest { enabled ->
                if (enabled) {
                    while (true) {
                        dao.getCoins().collect { list ->
                            list.map { coin ->
                                if (coin.marketId.equals("KRW-UPP")) {
                                    return@map
                                }
                                delay(1000)

                                val response = upbitService.fetchLatestCandles(
                                    unit = 1,
                                    market = coin.marketId,
                                    count = 10
                                )
                                val tradePrices = response.map { it.tradePrice }

                                val previousPrice = tradePrices[tradePrices.lastIndex - 1]
                                val nowPrice = tradePrices.last()

                                if (nowPrice >= previousPrice * 1.10) {
                                    // 가격 급등
                                    Timber.i("${coin.koreanName} 가격 급등 포착!!!")

                                    val accessToken = getAccessToken(
                                        "coinalert-gcp@coin-news-418815.iam.gserviceaccount.com\n",
                                        "https://www.googleapis.com/auth/cloud-platform"
                                    )
                                    googleCloudService.sendPushNotificaiton(
                                        authorization = "bearer ${accessToken}",
                                        request = SendNotificationRequest(
                                            "종목명 : ${coin.koreanName} 가격 급등",
                                            "가격 변동성이 포착되었습니다!!",
                                            token
                                        )
                                    )
                                } else {
                                    Timber.i("가격 변동성 없음 : ${coin.koreanName}")
                                }
                            }
                        }
                        delay(10_000L)
                    }
                }

            }
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
        builder.setContentText("거래량, 시세 모니터링")
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

        notificationManager.notify(1, builder.build()) // id : 정의해야하는 각 알림의 고유한 int값
        val notification = builder.build()
        startForeground(1, notification)
        notificationManager.notify(1, notification)
    }
}