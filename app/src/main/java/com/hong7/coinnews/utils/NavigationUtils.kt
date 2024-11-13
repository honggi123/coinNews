package com.hong7.coinnews.utils

import android.content.Intent
import android.net.Uri
import androidx.navigation.NavHostController
import com.hong7.coinnews.model.News


object NavigationUtils {

    fun navigate(
        controller: NavHostController,
        routeName: String,
        backStackRouteName: String? = null,
        isLaunchSingleTop: Boolean = true,
        needToRestoreState: Boolean = true
    ) {
        controller.navigate(routeName) {
            if (backStackRouteName != null) {
                popUpTo(backStackRouteName) { saveState = true }
            }
            launchSingleTop = isLaunchSingleTop
            restoreState = needToRestoreState
        }
    }
}