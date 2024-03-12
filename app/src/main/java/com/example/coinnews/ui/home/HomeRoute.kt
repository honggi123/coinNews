package com.example.coinnews.ui.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import com.example.coinnews.model.Coin

@Composable
fun HomeRoute(
    onCoinClick: (Coin) -> Unit
) {
    val tabContent = rememberTabContent(onCoinClick = onCoinClick)
    val (section, onSectionChange) = rememberSaveable {
        mutableStateOf(tabContent.first().section)
    }

    HomeScreen(
        tabs = tabContent,
        selectedSection = section,
        onSectionChange = onSectionChange,
        modifier = Modifier.fillMaxSize()
    )
}