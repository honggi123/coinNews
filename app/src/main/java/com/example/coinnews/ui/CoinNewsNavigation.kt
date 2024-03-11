package com.example.coinnews.ui

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Screen(
    val route: String,
    val navArguments: List<NamedNavArgument> = emptyList()
) {
    object Home : Screen("home")

    object CoinDetail : Screen(
        route = "coinDetail/{coinId}",
        navArguments = listOf(navArgument("coinId") {
            type = NavType.StringType
        })
    ) {
        fun createRoute(coinId: String) = "coinDetail/${coinId}"
    }
}