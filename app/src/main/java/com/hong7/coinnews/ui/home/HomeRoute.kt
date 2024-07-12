package com.hong7.coinnews.ui.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.hong7.coinnews.model.NetworkState
import com.hong7.coinnews.ui.CoinListNav
import com.hong7.coinnews.ui.ScrapNav
import com.hong7.coinnews.ui.SettingNav
import com.hong7.coinnews.utils.NavigationUtils
import kotlinx.collections.immutable.toImmutableList

@Composable
fun HomeRoute(
    networkState: NetworkState,
    navController: NavHostController
) {
    val tabContent = rememberTabContent(networkState, navController)
    val (section, onSectionChange) = rememberSaveable {
        mutableStateOf(tabContent.first().section)
    }


    HomeScreen(
        tabs = tabContent.toImmutableList(),
        selectedSection = section,
        onScrapListClick = {
            NavigationUtils.navigate(
                navController,
                ScrapNav.route
            )
        },
        onSettingClick = {
            NavigationUtils.navigate(navController, SettingNav.route)
        },
        onSectionChange = onSectionChange,
    )
}