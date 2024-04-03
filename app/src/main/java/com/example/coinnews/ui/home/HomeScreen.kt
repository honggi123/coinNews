package com.example.coinnews.ui.home

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.coinnews.R
import com.example.coinnews.model.Article
import com.example.coinnews.ui.articlelist.ArticleListScreen
import com.example.coinnews.ui.articlelist.ArticleListViewModel
import com.example.coinnews.ui.extensions.customTabIndicatorOffset
import com.example.coinnews.ui.scrap.ScrapNewsScreen
import com.example.coinnews.ui.theme.Grey1000
import com.example.coinnews.ui.theme.Grey200
import com.example.coinnews.ui.videolist.VideoListScreen
import kotlin.math.roundToInt

enum class Sections(@StringRes val titleResId: Int) {
    News(R.string.news),
    Video(R.string.video),
    Scrap(R.string.scrap),
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
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                },
                scrollBehavior = scrollBehavior,
                modifier = Modifier.background(MaterialTheme.colorScheme.background)
            )
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
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
    val tabWidths: List<Dp> = listOf(40.dp, 60.dp, 90.dp) // todo

    Column(
        modifier = modifier.padding(contentPadding),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        TabRow(
            selectedTabIndex = selectedTabIndex,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    modifier = Modifier
                        .customTabIndicatorOffset(
                            tabPositions[selectedTabIndex],
                            tabWidths[selectedTabIndex]
                        )
                        .graphicsLayer {
                            shape = RoundedCornerShape(
                                topStart = 16.dp,
                                topEnd = 16.dp,
                                bottomStart = 16.dp,
                                bottomEnd = 16.dp
                            )
                            clip = true
                        },
                    color = Grey1000
                )
            },
        ) {
            HomeTabRowContent(
                tabs,
                selectedTabIndex,
                onSectionChange
            )
        }
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
        val colorText = if (selectedTabIndex == index) {
            Grey1000
        } else {
            Grey200
        }
        Tab(
            selected = selectedTabIndex == index,
            onClick = { onSectionChange(content.section) },
            modifier = Modifier.height(40.dp)
        ) {
            Text(
                text = stringResource(id = content.section.titleResId),
                color = colorText,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun rememberTabContent(
    onArticleClick: (Article) -> Unit,
): List<TabContent> {
    val articleSection = TabContent(Sections.News) {
        ArticleListScreen(
            onArticleClick = onArticleClick,
        )
    }

    val videoSection = TabContent(Sections.Video) {
        VideoListScreen()
    }

    val scrapNewsSection = TabContent(Sections.Scrap) {
        ScrapNewsScreen(
            onArticleClick = onArticleClick
        )
    }

    return listOf(articleSection, videoSection, scrapNewsSection)
}



