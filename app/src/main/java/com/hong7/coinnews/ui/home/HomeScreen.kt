package com.hong7.coinnews.ui.home

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.hong7.coinnews.R
import com.hong7.coinnews.ui.articlelist.ArticleListScreen
import com.hong7.coinnews.ui.extensions.customTabIndicatorOffset
import com.hong7.coinnews.ui.scrap.ScrapNewsScreen
import com.hong7.coinnews.ui.theme.Grey1000
import com.hong7.coinnews.ui.theme.Grey200
import com.hong7.coinnews.ui.videolist.VideoListScreen

enum class Sections(@StringRes val titleResId: Int) {
    News(R.string.news),

    //    Video(R.string.video),
    Scrap(R.string.scrap),
}

class TabContent(val section: Sections, val content: @Composable () -> Unit)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    tabs: List<TabContent>,
    selectedSection: Sections,
    onSectionChange: (Sections) -> Unit,
) {
//    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
//        rememberTopAppBarState()
//    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Grey1000
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
//                scrollBehavior = scrollBehavior
            )
        },
//        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { contentPadding ->
        HomeScreenContent(
            tabs = tabs,
            selectedSection = selectedSection,
            onSectionChange = onSectionChange,
            modifier = Modifier.padding(horizontal = 16.dp),
            contentPadding = contentPadding,
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
        modifier = modifier
            .padding(contentPadding),
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
            containerColor = MaterialTheme.colorScheme.background
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
    navController: NavHostController
): List<TabContent> {
    val articleSection = TabContent(Sections.News) {
        ArticleListScreen(navController)
    }

//    val videoSection = TabContent(Sections.Video) {
//        VideoListScreen()
//    }

    val scrapNewsSection = TabContent(Sections.Scrap) {
        ScrapNewsScreen(navController)
    }

    return listOf(articleSection, scrapNewsSection)
}



