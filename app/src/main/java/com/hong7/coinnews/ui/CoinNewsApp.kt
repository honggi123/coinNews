package com.hong7.coinnews.ui

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.Firebase
import com.google.firebase.crashlytics.crashlytics
import com.google.firebase.messaging.FirebaseMessaging
import com.hong7.coinnews.preference.PreferenceManager
import com.hong7.coinnews.ui.feature.deprecated.info.InfoRoute
import com.hong7.coinnews.ui.feature.market.market.MarketScreen
import com.hong7.coinnews.ui.feature.newslist.NewsListScreen
import com.hong7.coinnews.ui.feature.setting.SettingScreen
import com.hong7.coinnews.ui.feature.videolist.VideoListScreen
import com.hong7.coinnews.ui.theme.Blue600
import com.hong7.coinnews.ui.theme.CoinNewsAppTheme
import com.hong7.coinnews.ui.theme.Grey500
import timber.log.Timber

@Composable
fun CoinNewsApp() {
    val context = LocalContext.current
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        CoinNewsAppTheme {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                snackbarHost = { SnackbarHost(snackbarHostState) },
                bottomBar = {
                    MyBottomNavigation(
                        containerColor = Color.White,
                        contentColor = Grey500,
                        navController = navController,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                    )
                }
            ) {
                Box(modifier = Modifier.padding(it)) {
                    CoinNewsNavGraph(
                        navController = navController,
                        snackbarHostState = snackbarHostState
                    )
                }
            }
        }
    }
}

@Composable
private fun CoinNewsNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    snackbarHostState: SnackbarHostState
) {
    NavHost(
        navController = navController,
        startDestination = MainNav.Info.route,
        modifier = modifier
    ) {
        composable(
            route = MainNav.Info.route,
        ) {
            InfoRoute(navController, snackbarHostState)
        }
        composable(
            route = MainNav.Setting.route,
        ) {
            SettingScreen(
                navController,
                modifier.fillMaxSize()
            )
        }
        composable(
            route = MainNav.Market.route,
        ) {
            MarketScreen(
                navController,
                snackbarHostState,
                modifier.fillMaxSize()
            )
        }
        composable(
            route = NewsNav.route,
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
            NewsListScreen(
                navController = navController,
                snackbarHostState = snackbarHostState
            )
        }
//        composable(
//            route = VideoListNav.routeWithArgName(),
//            arguments = VideoListNav.arguments,
//            enterTransition = {
//                slideIntoContainer(
//                    animationSpec = tween(400),
//                    towards = AnimatedContentTransitionScope.SlideDirection.Left
//                )
//            },
//            exitTransition = {
//                slideOutOfContainer(
//                    animationSpec = tween(400),
//                    towards = AnimatedContentTransitionScope.SlideDirection.Right
//                )
//            }
//        ) {
//            VideoListScreen(
//                snackbarHostState = snackbarHostState,
//                navController = navController
//            )
//        }

    }
}

@Composable
private fun MyBottomNavigation(
    containerColor: Color,
    contentColor: Color,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val items = listOf(
        MainNav.Info,
        MainNav.Market,
        MainNav.Setting
    )

    AnimatedVisibility(
        visible = items.map { it.route }.contains(currentRoute)
    ) {
        NavigationBar(
            modifier = modifier,
            containerColor = containerColor,
            contentColor = contentColor,
        ) {
            items.forEach { item ->
                NavigationBarItem(
                    selected = currentRoute == item.route,
                    label = {
                        Text(
                            text = item.title,
                            color = if (currentRoute == item.route) Blue600 else contentColor,
                            style = TextStyle(
                                fontSize = 9.sp
                            )
                        )
                    },
                    icon = {
                        Icon(
                            painter = painterResource(id = item.icon),
                            contentDescription = item.title,
                            tint = if (currentRoute == item.route) Blue600 else contentColor,
                            modifier = Modifier
                                .width(26.dp)
                                .height(26.dp)

                        )
                    },
                    onClick = {
                        navController.navigate(item.route) {
                            navController.graph.startDestinationRoute?.let {
                                popUpTo(it) { saveState = true }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = Color.Transparent
                    )
                )
            }
        }
    }
}
