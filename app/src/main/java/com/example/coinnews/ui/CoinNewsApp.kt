package com.example.coinnews.ui

import android.util.Log
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.coinnews.ui.articlelist.detail.ArticleDetailRoute
import com.example.coinnews.ui.home.HomeRoute
import com.example.coinnews.ui.theme.CoinNewsAppTheme
import com.example.coinnews.ui.utils.NavigationUtils

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
            HomeRoute(
                onArticleClick = {
                    NavigationUtils.navigate(
                        navController,
                        ArticleDetailNav.navigateWithArg(it)
                    )
                }
            )
        }
        composable(
            route = ArticleDetailNav.routeWithArgName(),
            arguments = ArticleDetailNav.arguments,
            enterTransition = {
                slideIntoContainer(
                    animationSpec = tween(400),
                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    animationSpec = tween(400),
                    towards = AnimatedContentTransitionScope.SlideDirection.End
                )
            }
        ) { navBackStackEntry ->
            val article = ArticleDetailNav.findArgument(navBackStackEntry)
            ArticleDetailRoute(
                article = article,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
