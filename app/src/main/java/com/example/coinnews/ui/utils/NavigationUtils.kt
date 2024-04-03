package com.example.coinnews.ui.utils

import androidx.navigation.NavHostController
import com.example.coinnews.model.Article


object NavigationUtils {

    private var article: Article? = null

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

    fun saveArticle(newArticle: Article) {
        article = newArticle
    }

    fun getSavedArticle(): Article? {
        return article
    }
}