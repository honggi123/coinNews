package com.example.coinnews.ui.news

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle


@Composable
fun NewsRoute(
    modifier: Modifier = Modifier,
    viewModel: NewsViewModel = hiltViewModel(),
) {
    val tabContent = rememberTabContent(viewModel)
    val (category, onCategoryChange) = rememberSaveable {
        mutableStateOf(tabContent.first().category)
    }

    NewsScreen(
        tabs = tabContent,
        selectedCategory = category,
        onCategoryChange = onCategoryChange,
        modifier = modifier
    )
}