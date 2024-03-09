package com.example.coinnews.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.coinnews.ui.home.HomeRoute
import com.example.coinnews.ui.theme.CoinNewsAppTheme

@Composable
fun CoinNewsApp() {
    CoinNewsAppTheme {
        CoinNewsNavGraph()
    }
}

@Composable
fun CoinNewsNavGraph(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = CoinNewsDestinations.NEWS_ROUTE,
        modifier = modifier,
    ) {
        composable(CoinNewsDestinations.NEWS_ROUTE) {
            HomeRoute(
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
