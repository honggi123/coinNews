package com.hong7.coinnews.ui.feature.market

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.hong7.coinnews.R
import com.hong7.coinnews.ui.component.SelectableChip
import com.hong7.coinnews.ui.feature.bithumb.BithumbScreen
import com.hong7.coinnews.ui.feature.newslist.NewsListScreen
import com.hong7.coinnews.ui.feature.upbit.UpbitScreen
import com.hong7.coinnews.ui.feature.videolist.VideoListScreen
import com.hong7.coinnews.ui.theme.Blue800
import com.hong7.coinnews.ui.theme.Grey1000
import com.hong7.coinnews.ui.theme.Grey200
import com.hong7.coinnews.ui.theme.Grey700
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.launch

enum class Sections(@StringRes val titleResId: Int) {
    Upbit(R.string.upbit),
    Bithumb(R.string.bithumb),
}

class TabContent(val section: Sections, val content: @Composable () -> Unit)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketScreen(
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
                        text = "마켓",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold
                        ),
                        color = Grey700
                    )
                },
                actions = {
                    TabRow(
                        selectedTabIndex = pagerState.currentPage,
                        indicator = { tabPositions ->

                        },
                        containerColor = MaterialTheme.colorScheme.background,
                        modifier = Modifier.width(200.dp)
                            .padding(horizontal = 8.dp)
                    ) {
                        HomeTabRowContent(
                            tabs,
                            pagerState.currentPage,
                            onSectionIndexChange = { coroutineScope.launch { pagerState.animateScrollToPage(it) } },
                        )
                    }
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
            contentPadding = contentPadding,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
private fun HomeScreenContent(
    tabs: ImmutableList<TabContent>,
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    Column(
        modifier = modifier
            .padding(contentPadding),
        horizontalAlignment = Alignment.End
    ) {

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
//        Tab(
//            selected =
//            onClick =
//                modifier =
//        ) {
//            Text(
//                text =,
//                color = colorText,
//                style = MaterialTheme.typography.titleMedium,
//                fontWeight = FontWeight.Bold
//            )
//        }
        SelectableChip(
            selected = selectedTabIndex == index,
            onClick = { onSectionIndexChange(index) },
            text = stringResource(id = content.section.titleResId),
            modifier = Modifier.width(40.dp)
        )
    }
}

@Composable
fun rememberTabContent(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
): List<TabContent> {

    val recentNewsSection = TabContent(Sections.Upbit) {
        UpbitScreen(navController, snackbarHostState)
    }

    val myCoinNewsSection = TabContent(Sections.Bithumb) {
        BithumbScreen(navController, snackbarHostState)
    }

    return listOf(recentNewsSection, myCoinNewsSection)
}
