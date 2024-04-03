package com.example.coinnews.ui.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.coinnews.model.Article
import com.example.coinnews.model.Coin
import com.example.coinnews.ui.articlelist.ArticleListViewModel

@Composable
fun HomeRoute(
    onArticleClick: (Article) -> Unit,
) {
    val tabContent = rememberTabContent(
        onArticleClick = onArticleClick
    )
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