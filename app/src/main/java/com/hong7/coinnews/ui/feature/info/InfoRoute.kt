package com.hong7.coinnews.ui.feature.deprecated.info

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import kotlinx.collections.immutable.toImmutableList

@Composable
fun InfoRoute(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState
) {
    val tabContent = rememberTabContent(navController, snackbarHostState)

    InfoScreen(
        tabs = tabContent.toImmutableList(),
    )
}