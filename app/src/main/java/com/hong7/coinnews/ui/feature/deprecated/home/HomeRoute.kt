//package com.hong7.coinnews.ui.feature.deprecated.home
//
//import androidx.compose.runtime.Composable
//import androidx.navigation.NavHostController
//import com.hong7.coinnews.ui.ScrapNav
//import com.hong7.coinnews.ui.SettingNav
//import com.hong7.coinnews.utils.NavigationUtils
//import kotlinx.collections.immutable.toImmutableList
//
//@Composable
//fun HomeRoute(
//    navController: NavHostController
//) {
//    val tabContent = rememberTabContent(navController)
//
//    HomeScreen(
//        tabs = tabContent.toImmutableList(),
//        onScrapListClick = {
//            NavigationUtils.navigate(
//                navController,
//                ScrapNav.route
//            )
//        },
//        onSettingClick = {
//            NavigationUtils.navigate(navController, SettingNav.route)
//        }
//    )
//}