package com.hong7.coinnews.ui

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hong7.coinnews.ui.articledetail.ArticleDetailRoute
import com.hong7.coinnews.ui.coinlist.CoinListScreen
import com.hong7.coinnews.ui.home.HomeRoute
import com.hong7.coinnews.ui.setting.SettingScreen
import com.hong7.coinnews.ui.theme.CoinNewsAppTheme
import com.hong7.coinnews.utils.NavigationUtils

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
        startDestination = HomeNav.route,
        modifier = modifier
    ) {
        composable(
            route = HomeNav.route
        ) {
            HomeRoute(navController)
        }
        composable(
            route = ArticleDetailNav.routeWithArgName(),
            arguments = ArticleDetailNav.arguments,
            enterTransition = {
                slideIntoContainer(
                    animationSpec = tween(400),
                    towards = AnimatedContentTransitionScope.SlideDirection.Left
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    animationSpec = tween(400),
                    towards = AnimatedContentTransitionScope.SlideDirection.Right
                )
            }
        ) {
            val article = NavigationUtils.getSavedArticle()

            ArticleDetailRoute(
                article = article,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        composable(
            route = CoinListNav.route,
            enterTransition = {
                slideIntoContainer(
                    animationSpec = tween(400),
                    towards = AnimatedContentTransitionScope.SlideDirection.Left
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    animationSpec = tween(400),
                    towards = AnimatedContentTransitionScope.SlideDirection.Right
                )
            }
        ) {
            CoinListScreen(
                navController,
                modifier.fillMaxSize()
            )
        }
        composable(
            route = SettingNav.route,
            enterTransition = {
                slideIntoContainer(
                    animationSpec = tween(400),
                    towards = AnimatedContentTransitionScope.SlideDirection.Left
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    animationSpec = tween(400),
                    towards = AnimatedContentTransitionScope.SlideDirection.Right
                )
            }
        ) {
            SettingScreen(
                navController,
                modifier.fillMaxSize()
            )
        }
    }
}
