package com.hong7.coinnews.ui.main

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.Firebase
import com.google.firebase.crashlytics.crashlytics
import com.google.firebase.messaging.FirebaseMessaging
import com.hong7.coinnews.BuildConfig
import com.hong7.coinnews.preference.PreferenceManager
import com.hong7.coinnews.service.monitor.CoinMonitorForegroundService
import com.hong7.coinnews.ui.CoinNewsApp
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var preferenceManager: PreferenceManager

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Thread.setDefaultUncaughtExceptionHandler(UncaughtExceptionHandler(this))

        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition {
            mainViewModel.isLoading.value
        }

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Timber.e("Fetching FCM registration token failed", task.exception)
                throw task.exception ?: Exception()
                return@OnCompleteListener
            }
            val token = task.result
            Timber.tag("Token").i(token)
            mainViewModel.saveFcmToken(token)
        })

        startMonitorService(preferenceManager, this@MainActivity)

        setContent {
            CoinNewsApp()
        }
    }
}

fun startMonitorService(preferenceManager: PreferenceManager, context: Context){
    val serviceIntent = Intent(context, CoinMonitorForegroundService::class.java)

    GlobalScope.launch {
        combine(
            preferenceManager.getCoinVolumeAlertEnabled(),
            preferenceManager.getCoinPriceChangeAlertEnabled()
        ) { volumeEnabled, priceEnabled ->
            volumeEnabled || priceEnabled
        }.collectLatest { enabled ->
            if (enabled) {
                ContextCompat.startForegroundService(context, serviceIntent)
            } else {
                context.stopService(serviceIntent)
            }
        }
    }

}

class UncaughtExceptionHandler(
    val context: Context,
) : Thread.UncaughtExceptionHandler {
    override fun uncaughtException(thread: Thread, throwable: Throwable) {
        Firebase.crashlytics.recordException(throwable)
        Timber.tag("uncaughtException").e(throwable)
        Toast.makeText(context, "현재 시스템 오류가 발생했습니다. 앱을 재설치하거나, 잠시후 다시 시도해 주세요.", Toast.LENGTH_LONG)
            .show()
    }
}