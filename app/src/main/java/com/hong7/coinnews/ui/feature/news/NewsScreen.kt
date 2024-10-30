package com.hong7.coinnews.ui.feature.news

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.hong7.coinnews.R
import com.hong7.coinnews.model.News
import com.hong7.coinnews.model.Coin
import com.hong7.coinnews.model.Filter
import com.hong7.coinnews.ui.CoinListNav
import com.hong7.coinnews.ui.NewsDetailNav
import com.hong7.coinnews.ui.component.ClickableChip
import com.hong7.coinnews.ui.component.SelectableChip
import com.hong7.coinnews.ui.extensions.clickableWithoutRipple
import com.hong7.coinnews.ui.theme.Blue600
import com.hong7.coinnews.ui.theme.Blue800
import com.hong7.coinnews.ui.theme.Grey1000
import com.hong7.coinnews.ui.theme.Grey200
import com.hong7.coinnews.ui.theme.Grey300
import com.hong7.coinnews.ui.theme.Grey600
import com.hong7.coinnews.ui.theme.defaultTextStyle
import com.hong7.coinnews.utils.DateUtils
import com.hong7.coinnews.utils.NavigationUtils


@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun NewsScreen(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    viewModel: NewsViewModel = hiltViewModel()
) {
    val selectedCoin by viewModel.selectedCoin.collectAsStateWithLifecycle()
    val uistate by viewModel.uiState.collectAsStateWithLifecycle()
    val watchedNews by viewModel.watchedNewsIds.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.messageEvent.collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    when (val state = uistate) {
        is NewsScreenUiState.Success -> {
            NewsListScreenContent(
                watchedNewsIds = watchedNews,
                isNewsLoading = state.isNewsLoading,
                newsList = state.newsList,
                selectedCoin = selectedCoin,
                filter = state.filter,
                onCoinClick = remember(viewModel) { { viewModel.onCoinClick(it) } },
                onFilterSettingClick = {
                    NavigationUtils.navigate(
                        navController,
                        CoinListNav.route
                    )
                },
                onNewsClick = {
                    NavigationUtils.navigate(
                        navController,
                        NewsDetailNav.navigateWithArg(it)
                    )
                    viewModel.onNewsClick(it.id)
                },
                modifier = Modifier.fillMaxWidth(),
            )

        }

        is NewsScreenUiState.FilterEmpty -> {
            EmptyFilterContent(
                onFilterSettingClick = {
                    NavigationUtils.navigate(
                        navController,
                        CoinListNav.route
                    )
                },
                modifier = Modifier.fillMaxSize()
            )
        }

        else -> Unit
    }
}

@Composable
private fun EmptyFilterContent(
    onFilterSettingClick: () -> Unit,
    modifier: Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EmptyFiltersContent(
            text = "Select the coins for the news you want to see!"
        )
        Spacer(modifier = Modifier.height(10.dp))
        ClickableChip(
            text = "Select Coins",
            onClick = { onFilterSettingClick() },
        )
    }
}

@Composable
private fun LoadingContent(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(38.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NewsListScreenContent(
    watchedNewsIds: Set<String>,
    isNewsLoading: Boolean,
    filter: Filter,
    newsList: List<News>?,
    selectedCoin: Coin?,
    onFilterSettingClick: () -> Unit,
    onCoinClick: (Coin) -> Unit,
    onNewsClick: (News) -> Unit,
    modifier: Modifier = Modifier,
) {
    val state = rememberLazyListState()
    val filterState = rememberLazyListState()

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        rememberTopAppBarState()
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "News",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold
                        ),
                        color = Blue800
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
        Column(
            modifier = modifier.padding(contentPadding),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                HorizontalDivider(
                    thickness = 0.7.dp,
                    color = Grey200,
                )
                Spacer(modifier = Modifier.height(15.dp))
                CoinFiltersRow(
                    coins = filter.coins,
                    selectedCoin = selectedCoin,
                    onCoinClick = onCoinClick,
                    onFilterSettingClick = onFilterSettingClick,
                    state = filterState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(
                    thickness = 0.7.dp,
                    color = Grey200,
                )
                if (isNewsLoading) {
                    Column(
                        modifier = modifier,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(38.dp)
                        )
                    }
                } else {
                    NewsList(
                        watchedNewsIds = watchedNewsIds,
                        newsList = newsList ?: emptyList(),
                        onNewsClick = onNewsClick,
                        modifier = Modifier.fillMaxWidth(),
                        state = state
                    )
                }
            }
        }
    }
}

@Composable
private fun CoinFiltersRow(
    coins: List<Coin>,
    selectedCoin: Coin?,
    onCoinClick: (Coin) -> Unit,
    onFilterSettingClick: () -> Unit,
    state: LazyListState,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        LazyRow(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            state = state
        ) {
            items(coins.size, key = { coins[it].id }) {
                SelectableChip(
                    selected = coins[it] == selectedCoin,
                    text = if (coins[it].name == "암호화폐") "전체" else coins[it].name,
                    onClick = { onCoinClick(coins[it]) }
                )
            }
        }
        SettingButton(
            text = "설정",
            onClick = onFilterSettingClick
        )
    }
}

@Composable
private fun NewsList(
    watchedNewsIds: Set<String>,
    newsList: List<News>,
    onNewsClick: (News) -> Unit,
    state: LazyListState,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {

    LazyColumn(
        contentPadding = contentPadding,
        modifier = Modifier.fillMaxSize(),
        state = state
    ) {
        items(
            newsList.size,
//                        key = { newss[it].id } todo
        ) { index ->
            if (index == 0) {
                Spacer(modifier = Modifier.height(10.dp))
            }
            newsList[index].let {
                NewsContentItem(
                    watchedNewsIds = watchedNewsIds,
                    news = it,
                    onNewsClick = onNewsClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
                HorizontalDivider(
                    thickness = 0.7.dp,
                    color = Grey200,
                    modifier = Modifier.padding(vertical = 15.dp)
                )
            }
        }

    }
}

@Composable
private fun SettingButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 5.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Blue600,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun EmptyFiltersContent(
    text: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = text,
            style = defaultTextStyle.copy(
                fontSize = 16.sp,
                lineHeight = 20.sp,
            ),
            fontWeight = FontWeight.Medium,
        )
    }
}

@Composable
private fun NewsContentItem(
    watchedNewsIds: Set<String>,
    news: News,
    onNewsClick: (News) -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val titleColor = if (watchedNewsIds.contains(news.id)) {
        Color(0xFFAAAAAA)
    } else {
        Grey1000
    }

    Column(
        modifier = modifier.clickableWithoutRipple(
            interactionSource = interactionSource,
        ) {
            onNewsClick(news)
        },
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Text(
            text = news.title,
            style = defaultTextStyle.copy(
                fontSize = 16.sp,
                lineHeight = 20.sp,
            ),
            color = titleColor,
            fontWeight = FontWeight.Bold,
            maxLines = 3,
        )
        Text(
            text = news.description,
            style = defaultTextStyle.copy(
                fontSize = 16.sp,
                lineHeight = 20.sp,
            ),
            overflow = TextOverflow.Ellipsis,
            color = Grey600,
            fontWeight = FontWeight.Medium,
            maxLines = 3,
        )
        NewsMetaData(
            news = news,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun NewsMetaData(
    news: News,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(
            text = news.author + "・" + news.createdAt?.let { DateUtils.getTimeAgo(it) },
            style = defaultTextStyle.copy(
                fontSize = 14.sp,
                lineHeight = 14.sp,
            ),
            color = Color(0xFFAAAAAA)
        )
    }
}

//
//@Preview
//@Composable
//fun PreviewNewsContent(
//    @PreviewParameter(NewsContentPreviewParamProvider::class) newss: Flow<PagingData<News>>
//) {
//    CoinNewsAppTheme {
//        NewsListScreenContent(
//            newss = newss.collectAsLazyPagingItems(),
//            filters = listOf(
//                CoinFilter("비트코인", "BTC", symbol = ""),
//                CoinFilter("이더리움", "ETC", symbol = "")
//            ),
//            onCoinFilterClick = {},
//            onFilterSettingClick = {},
//            selectedFilter = Filter(CoinFilter("비트코인", "BTC", ""), CountryScope.Local),
//            onNewsClick = {},
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(10.dp)
//                .background(White)
//        )
//    }
//}
//
//private class NewsContentPreviewParamProvider :
//    PreviewParameterProvider<Flow<PagingData<News>>> {
//
//    override val values: Sequence<Flow<PagingData<News>>> =
//        sequenceOf(
//            flowOf(
//                PagingData.from(
//                    listOf(
//                        News(
//                            id = "1",
//                            title = "블롬버그 \"버블 조짐 있다\"vs 월스트리트저널 \"과거 만큼은 아니다\"",
//                            url = "url",
////                            description = "한편, 한화투자증권은 2021년 2월에 암호화폐 거래소 업비트와 주식 거래 플랫폼 증권플러스 등을 운영하는 두나무 보통주 약 200만주를 583억원에 매수한 바 있다.",
//                            author = "블록미디어",
//                            createdAt = Instant.now().toEpochMilli()
//                        )
//                    )
//                )
//            )
//        )
//}
