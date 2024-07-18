package com.hong7.coinnews.ui.main

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.firebase.Firebase
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.crashlytics.crashlytics
import com.hong7.coinnews.BuildConfig
import com.hong7.coinnews.model.NetworkState
import com.hong7.coinnews.ui.CoinNewsApp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {

        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)

        Firebase.crashlytics.setCustomKey("is_debug_mode", BuildConfig.DEBUG)

        splashScreen.setKeepOnScreenCondition() {
            mainViewModel.isLoading.value
        }

        setContent {
            CoinNewsApp(mainViewModel)
        }
    }
}
