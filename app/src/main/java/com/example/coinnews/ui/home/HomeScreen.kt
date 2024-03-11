package com.example.coinnews.ui.home

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
import com.example.coinnews.ui.articlelist.ArticleListScreen
import com.example.coinnews.ui.coinlist.CoinListScreen
import com.example.coinnews.ui.interest.InterestCoinListScreen

enum class Sections(@StringRes val titleResId: Int) {
    News(R.string.news),
    Coin(R.string.coin),
    InterestingCoin(R.string.interesting_coin),
}

class TabContent(val section: Sections, val content: @Composable () -> Unit)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    tabs: List<TabContent>,
    selectedSection: Sections,
    onSectionChange: (Sections) -> Unit,
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
        HomeScreenContent(
            tabs = tabs,
            selectedSection = selectedSection,
            onSectionChange = onSectionChange,
            modifier = Modifier.padding(horizontal = 16.dp),
            contentPadding = contentPadding
        )
    }
}

@Composable
fun HomeScreenContent(
    tabs: List<TabContent>,
    selectedSection: Sections,
    onSectionChange: (Sections) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val selectedTabIndex = tabs.indexOfFirst { it.section == selectedSection }

    Column(
        modifier = modifier.padding(contentPadding)
    ) {
        TabRow(
            selectedTabIndex = selectedTabIndex,
            contentColor = MaterialTheme.colorScheme.primary
        ) {
            HomeTabRowContent(
                tabs,
                selectedTabIndex,
                onSectionChange
            )
        }
        Spacer(modifier = Modifier.height(15.dp))
        tabs[selectedTabIndex].content()
    }
}

@Composable
fun HomeTabRowContent(
    tabs: List<TabContent>,
    selectedTabIndex: Int,
    onSectionChange: (Sections) -> Unit,
) {
    tabs.forEachIndexed { index, content ->
        Tab(
            selected = selectedTabIndex == index,
            onClick = { onSectionChange(content.section) },
            modifier = Modifier.height(50.dp)
        ) {
            Text(
                text = stringResource(id = content.section.titleResId),
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }
}

@Composable
fun rememberTabContent(
    onCoinClick: (String) -> Unit
): List<TabContent> {
    val articleSection = TabContent(Sections.News) {
        ArticleListScreen()
    }

    val coinSection = TabContent(Sections.Coin) {
        CoinListScreen(onCoinClick = onCoinClick)
    }

    val interestingCoinSection = TabContent(Sections.InterestingCoin) {
        InterestCoinListScreen()
    }

    return listOf(coinSection, articleSection, interestingCoinSection)
}



