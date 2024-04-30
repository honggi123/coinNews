package com.hong7.coinnews.ui.home

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.hong7.coinnews.R
import com.hong7.coinnews.ui.SettingNav
import com.hong7.coinnews.ui.articlelist.ArticleListScreen
import com.hong7.coinnews.ui.articlelist.ArticleListViewModel
import com.hong7.coinnews.ui.component.BaseCustomModal
import com.hong7.coinnews.ui.component.CheckListItem
import com.hong7.coinnews.ui.extensions.customTabIndicatorOffset
import com.hong7.coinnews.ui.scrap.ScrapNewsScreen
import com.hong7.coinnews.ui.theme.Blue600
import com.hong7.coinnews.ui.theme.Blue800
import com.hong7.coinnews.ui.theme.Grey1000
import com.hong7.coinnews.ui.theme.Grey200
import com.hong7.coinnews.ui.theme.Grey500
import com.hong7.coinnews.ui.theme.Grey700
import com.hong7.coinnews.ui.theme.GreyOpacity400
import com.hong7.coinnews.utils.NavigationUtils
import dagger.hilt.android.lifecycle.HiltViewModel

enum class Sections(@StringRes val titleResId: Int) {
    News(R.string.recent_news),
    //    Video(R.string.video),
    Scrap(R.string.scrap),
}

class TabContent(val section: Sections, val content: @Composable () -> Unit)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    tabs: MutableList<TabContent>,
    selectedSection: Sections,
    onSettingClick: () -> Unit,
    onSectionChange: (Sections) -> Unit,
    viewModel: ArticleListViewModel = hiltViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        rememberTopAppBarState()
    )

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
            selectedSection = selectedSection,
            onSectionChange = onSectionChange,
            contentPadding = contentPadding,
        )
    }
}

@Composable
fun HomeScreenContent(
    tabs: MutableList<TabContent>,
    selectedSection: Sections,
    onSectionChange: (Sections) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val selectedTabIndex = tabs.indexOfFirst { it.section == selectedSection }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(contentPadding),
    ) {
        TabRow(
            selectedTabIndex = selectedTabIndex,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                    color = Blue800
                )
            },
            containerColor = MaterialTheme.colorScheme.background,
        ) {
            HomeTabRowContent(
                tabs,
                selectedTabIndex,
                onSectionChange,
            )
        }
        tabs[selectedTabIndex].content()
    }
}

@Composable
fun HomeTabRowContent(
    tabs: MutableList<TabContent>,
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
    navController: NavHostController
): MutableList<TabContent> {
    val articleSection = TabContent(Sections.News) {
        ArticleListScreen(navController)
    }

//    val videoSection = TabContent(Sections.Video) {
//        VideoListScreen()
//    }

    val scrapNewsSection = TabContent(Sections.Scrap) {
        ScrapNewsScreen(navController)
    }

    return mutableListOf(articleSection, scrapNewsSection)
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


