package com.example.coinnews.ui.news

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.coinnews.R
import com.example.coinnews.model.Article
import com.example.coinnews.model.ArticleMetaData
import com.example.coinnews.ui.news.components.ArticleContent
import com.example.coinnews.ui.theme.CoinNewsAppTheme

enum class Categorys(@StringRes val titleResId: Int) {
    News(R.string.news),
    Twitter(R.string.twitter),
}

class TabContent(val category: Categorys, val content: @Composable () -> Unit)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsScreen(
    tabs: List<TabContent>,
    selectedCategory: Categorys,
    onCategoryChange: (Categorys) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.news),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        modifier = modifier
    ) { contentPadding ->
        NewsScreenContent(
            tabs = tabs,
            selectedCategory = selectedCategory,
            onCategoryChange = onCategoryChange,
            modifier = Modifier.padding(horizontal = 16.dp),
            contentPadding = contentPadding
        )
    }
}

@Composable
fun NewsScreenContent(
    tabs: List<TabContent>,
    selectedCategory: Categorys,
    onCategoryChange: (Categorys) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val selectedTab = tabs.find { it.category == selectedCategory}

    // todo add tab
    Column(
        modifier = modifier.padding(contentPadding)
    ) {
        selectedTab?.let { it.content() }
    }
}

@Composable
fun rememberTabContent(newsViewModel: NewsViewModel): List<TabContent> {
    val newsSection = TabContent(Categorys.News) {
        val articles = newsViewModel.articles.collectAsLazyPagingItems()
        ArticleContent(
            articles = articles,
            onArticleClick = { createArticleIntent(it) }
        )
    }

    val twitterSection = TabContent(Categorys.Twitter) {
        val twitterPosts = newsViewModel.twitterPosts.collectAsLazyPagingItems()
    }

    return listOf(newsSection, twitterSection)
}

private fun createArticleIntent(article: Article) {}

