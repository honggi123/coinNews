package com.hong7.coinnews.ui.feature.market

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import kotlinx.collections.immutable.toImmutableList

@Composable
fun MarketRoute(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState
) {
    val tabContent = rememberTabContent(navController, snackbarHostState)

    MarketScreen(
        tabs = tabContent.toImmutableList(),
    )
}