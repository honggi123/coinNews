package com.example.coinnews.ui.news

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.coinnews.R
import com.example.coinnews.model.Article
import com.example.coinnews.ui.news.components.ArticleContent
import com.example.coinnews.ui.news.components.CoinContent

enum class Categorys(@StringRes val titleResId: Int) {
    News(R.string.news),
    Coin(R.string.coin),
    InterestingCoin(R.string.interesting_coin),
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
    val selectedTabIndex = tabs.indexOfFirst { it.category == selectedCategory }

    Column(
        modifier = modifier.padding(contentPadding)
    ) {
        TabRow(
            selectedTabIndex = selectedTabIndex,
            contentColor = MaterialTheme.colorScheme.primary
        ) {
            NewsTabRowContent(
                tabs,
                selectedTabIndex,
                onCategoryChange
            )
        }
        Spacer(modifier = Modifier.height(15.dp))
        tabs[selectedTabIndex].content()
    }
}

@Composable
fun NewsTabRowContent(
    tabs: List<TabContent>,
    selectedTabIndex: Int,
    onCategoryChange: (Categorys) -> Unit
) {
    tabs.forEachIndexed { index, content ->
        Tab(
            selected = selectedTabIndex == index,
            onClick = { onCategoryChange(content.category) },
            modifier = Modifier.height(50.dp)
        ) {
            Text(
                text = stringResource(id = content.category.titleResId),
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }
}

@Composable
fun rememberTabContent(newsViewModel: NewsViewModel): List<TabContent> {
    val context = LocalContext.current

    val newsSection = TabContent(Categorys.News) {
        val articles = newsViewModel.articles.collectAsLazyPagingItems()
        ArticleContent(
            articles = articles,
            onArticleClick = { createArticleIntent(it, context) }
        )
    }

    val coinSection = TabContent(Categorys.Coin) {
        val coins = newsViewModel.coins.collectAsLazyPagingItems()
        val selectedSort by newsViewModel.selectedSort.collectAsStateWithLifecycle()

        CoinContent(
            selectedSort = selectedSort,
            onSortingClick = newsViewModel::onSortClick,
            coins = coins
        )
    }

    val interestingCoinSection = TabContent(Categorys.InterestingCoin) {
        // TODO
    }

    return listOf(coinSection, newsSection, interestingCoinSection)
}

private fun createArticleIntent(article: Article, context: Context) {
    context.startActivity(
        Intent(Intent.ACTION_VIEW)
            .setData(Uri.parse(article.url))
            .setPackage("com.google.android.youtube")
    )
}

