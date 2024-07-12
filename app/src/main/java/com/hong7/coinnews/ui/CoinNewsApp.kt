package com.hong7.coinnews.ui

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hong7.coinnews.model.NetworkState
import com.hong7.coinnews.ui.coinlist.CoinListScreen
import com.hong7.coinnews.ui.home.HomeRoute
import com.hong7.coinnews.ui.main.MainViewModel
import com.hong7.coinnews.ui.newsdetail.NewsDetailRoute
import com.hong7.coinnews.ui.scrap.ScrapNewsScreen
import com.hong7.coinnews.ui.setting.SettingScreen
import com.hong7.coinnews.ui.theme.CoinNewsAppTheme

@Composable
fun CoinNewsApp(viewModel: MainViewModel) {
    val networkState by viewModel.networkState.collectAsState(initial = NetworkState.None)

    val snackbarHostState = remember {
        SnackbarHostState()
    }

    LaunchedEffect(key1 = networkState) {
        if (networkState is NetworkState.NotConnected) {
            snackbarHostState.showSnackbar(
                "인터넷이 연결되지 않았습니다.", "확인", SnackbarDuration.Indefinite
            )
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        CoinNewsAppTheme {
            CoinNewsNavGraph(networkState)
        }
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun CoinNewsNavGraph(
    networkState: NetworkState,
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
            HomeRoute(networkState, navController)
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
            CoinListScreen(
                navController,
                modifier.fillMaxSize()
            )
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
