package com.hong7.coinnews.ui.home

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.hong7.coinnews.R
import com.hong7.coinnews.ui.CoinListNav
import com.hong7.coinnews.ui.mycoinnews.MyCoinNewsScreen
import com.hong7.coinnews.ui.mycoinnews.MyCoinNewsViewModel
import com.hong7.coinnews.ui.recentcoinnews.RecentCoinNewsScreen
import com.hong7.coinnews.ui.scrap.ScrapNewsScreen
import com.hong7.coinnews.ui.theme.Blue800
import com.hong7.coinnews.ui.theme.Grey1000
import com.hong7.coinnews.ui.theme.Grey200
import com.hong7.coinnews.ui.theme.Grey500
import com.hong7.coinnews.utils.NavigationUtils
import kotlinx.coroutines.launch

enum class Sections(@StringRes val titleResId: Int) {
    RecnentNews(R.string.recent_news),
    MyCoinNews(R.string.my_coin_news),
}

class TabContent(val section: Sections, val content: @Composable () -> Unit)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    tabs: MutableList<TabContent>,
    selectedSection: Sections,
    onScrapListClick: () -> Unit,
    onSettingClick: () -> Unit,
    onSectionChange: (Sections) -> Unit,
    viewModel: MyCoinNewsViewModel = hiltViewModel()
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
                        text = "코인왓치",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold
                        ),
                        color = Blue800
                    )
                },
                actions = {
                    IconButton(
                        onClick = { onScrapListClick() },
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_bookmarks),
                            contentDescription = "",
                            tint = Grey500
                        )
                    }
                    IconButton(
                        onClick = { onSettingClick() },
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_setting),
                            contentDescription = "",
                            tint = Grey500
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
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
    tabs: MutableList<TabContent>,
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
    tabs: MutableList<TabContent>,
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
    navController: NavHostController
): MutableList<TabContent> {

    val recentNewsSection = TabContent(Sections.RecnentNews) {
        RecentCoinNewsScreen(navController)
    }

    val myCoinNewsSection = TabContent(Sections.MyCoinNews) {
        MyCoinNewsScreen(navController)
    }

    return mutableListOf(recentNewsSection, myCoinNewsSection)
}

//@OptIn(ExperimentalMaterial3Api::class)
//@SuppressLint("UnusedBoxWithConstraintsScope")
//@Composable
//private fun SettingBottomModal(
//    isGlobalNews: Boolean,
//    onCloseClick: () -> Unit,
//    onCompleteClick: (filter: Filter) -> Unit,
//    modifier: Modifier = Modifier,
//    sheetState: SheetState = rememberModalBottomSheetState(),
//) {
//    val filters = rememberSaveable { mutableStateOf(filter.coins) }
//    val scope = rememberSaveable { mutableStateOf(filter.scope) }
//
//    ModalBottomSheet(
//        modifier = Modifier.padding(horizontal = 10.dp),
//        sheetState = sheetState,
//        tonalElevation = 0.dp,
//        scrimColor = GreyOpacity400,
//        onDismissRequest = { onCloseClick() },
//        dragHandle = null,
//        containerColor = Color.Transparent,
//    ) {
//        BoxWithConstraints(
//            Modifier
//                .navigationBarsPadding()
//                .padding(bottom = 30.dp)
//        ) {
//            BaseCustomModal(
//                onDismissClick = onCloseClick,
//                actionButtonText = "등록",
//                onActionClick = {
//                    onCompleteClick(
//                        filter.copy(
//                            coins = filters.value,
//                            scope = scope.value
//                        )
//                    )
//                },
//                modifier = modifier,
//            ) {
//                Column(
//                    modifier = Modifier.fillMaxWidth(),
//                    verticalArrangement = Arrangement.spacedBy(6.dp)
//                ) {
//                    CheckListItem(
//                        checked = scope.value != CountryScope.Local,
//                        text = "해외 뉴스 (영어)",
//                        onClick = {
//                            scope.value = if (it) {
//                                CountryScope.Global
//                            } else {
//                                CountryScope.Local
//                            }
//                        }
//                    )
//                }
//            }
//        }
//    }
//}


