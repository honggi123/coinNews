package com.hong7.coinnews.ui.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.hong7.coinnews.ui.SettingNav
import com.hong7.coinnews.utils.NavigationUtils

@Composable
fun HomeRoute(
    navController: NavHostController
) {
    val tabContent = rememberTabContent(navController)
    val (section, onSectionChange) = rememberSaveable {
        mutableStateOf(tabContent.first().section)
    }

    HomeScreen(
        tabs = tabContent,
        selectedSection = section,
        onSettingClick = {
            NavigationUtils.navigate(navController, SettingNav.route)
        },
        onSectionChange = onSectionChange,
    )
}