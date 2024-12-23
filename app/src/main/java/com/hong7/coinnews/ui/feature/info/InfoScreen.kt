package com.hong7.coinnews.ui.feature.deprecated.info

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.hong7.coinnews.R
import com.hong7.coinnews.ui.feature.newslist.NewsListScreen
import com.hong7.coinnews.ui.feature.videolist.VideoListScreen
import com.hong7.coinnews.ui.theme.Blue800
import com.hong7.coinnews.ui.theme.Grey1000
import com.hong7.coinnews.ui.theme.Grey200
import com.hong7.coinnews.ui.theme.Grey700
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.launch

enum class Sections(@StringRes val titleResId: Int) {
    News(R.string.news),
    Video(R.string.video),
}

class TabContent(val section: Sections, val content: @Composable () -> Unit)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoScreen(
    tabs: ImmutableList<TabContent>,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        rememberTopAppBarState()
    )
    val pagerState = rememberPagerState(pageCount = { 2 })
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "코인 정보",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold
                        ),
                        color = Grey700
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    scrolledContainerColor = Color.White
                ),
                scrollBehavior = scrollBehavior
            )
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { contentPadding ->
        HomeScreenContent(
            tabs = tabs,
            pagerState = pagerState,
            onSectionIndexChange = { coroutineScope.launch { pagerState.animateScrollToPage(it) } },
            contentPadding = contentPadding,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
private fun HomeScreenContent(
    tabs: ImmutableList<TabContent>,
    pagerState: PagerState,
    onSectionIndexChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    Column(
        modifier = modifier
            .padding(contentPadding),
    ) {
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                    color = Blue800
                )
            },
            containerColor = MaterialTheme.colorScheme.background,
        ) {
            HomeTabRowContent(
                tabs,
                pagerState.currentPage,
                onSectionIndexChange,
            )
        }
        HorizontalPager(
            state = pagerState,
        ) {
            tabs[pagerState.currentPage].content()
        }
    }
}

@Composable
private fun HomeTabRowContent(
    tabs: ImmutableList<TabContent>,
    selectedTabIndex: Int,
    onSectionIndexChange: (Int) -> Unit,
) {
    tabs.forEachIndexed { index, content ->
        val colorText = if (selectedTabIndex == index) {
            Grey1000
        } else {
            Grey200
        }
        Tab(
            selected = selectedTabIndex == index,
            onClick = { onSectionIndexChange(index) },
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
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
): List<TabContent> {

    val recentNewsSection = TabContent(Sections.News) {
        NewsListScreen(navController, snackbarHostState)
    }

    val myCoinNewsSection = TabContent(Sections.Video) {
        VideoListScreen(navController)
    }

    return listOf(recentNewsSection, myCoinNewsSection)
}
