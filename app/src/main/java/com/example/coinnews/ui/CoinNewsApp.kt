package com.example.coinnews.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.coinnews.ui.coinlist.detail.CoinDetailRoute
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
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(Screen.Home.route) {
            HomeRoute(
                onCoinClick = {
                    navController.navigate(
                        Screen.CoinDetail.createRoute(it)
                    )
                }
            )
        }
        composable(
            route = Screen.CoinDetail.route,
            arguments = Screen.CoinDetail.navArguments
        ) {
            CoinDetailRoute(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
