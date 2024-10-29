package com.hong7.coinnews.ui

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hong7.coinnews.ui.feature.filtersetting.FilterSettingScreen
import com.hong7.coinnews.ui.feature.deprecated.home.HomeRoute
import com.hong7.coinnews.ui.feature.news.NewsScreen
import com.hong7.coinnews.ui.main.MainViewModel
import com.hong7.coinnews.ui.feature.newsdetail.NewsDetailRoute
import com.hong7.coinnews.ui.feature.scrap.ScrapNewsScreen
import com.hong7.coinnews.ui.feature.setting.SettingScreen
import com.hong7.coinnews.ui.theme.CoinNewsAppTheme

@Composable
fun CoinNewsApp(viewModel: MainViewModel) {

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        CoinNewsAppTheme {
            CoinNewsNavGraph()
        }
    }
}

@Composable
private fun CoinNewsNavGraph(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NewsNav.route,
        modifier = modifier
    ) {
        composable(
            route = NewsNav.route
        ) {
            NewsScreen(navController)
        }
        composable(
            route = NewsDetailNav.routeWithArgName(),
            arguments = NewsDetailNav.arguments,
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
            NewsDetailRoute(
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
            FilterSettingScreen(navController)
        }
        composable(
            route = ScrapNav.route,
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
            ScrapNewsScreen(
                navController,
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
