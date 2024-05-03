package com.hong7.coinnews.ui.mycoinnews

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.hong7.coinnews.model.Article
import com.hong7.coinnews.model.Coin
import com.hong7.coinnews.model.Filter
import com.hong7.coinnews.ui.ArticleDetailNav
import com.hong7.coinnews.ui.CoinListNav
import com.hong7.coinnews.ui.component.ClickableChip
import com.hong7.coinnews.ui.component.SelectableChip
import com.hong7.coinnews.ui.extensions.clickableWithoutRipple
import com.hong7.coinnews.ui.theme.Blue600
import com.hong7.coinnews.ui.theme.Grey1000
import com.hong7.coinnews.ui.theme.Grey200
import com.hong7.coinnews.ui.theme.defaultTextStyle
import com.hong7.coinnews.utils.DateUtils
import com.hong7.coinnews.utils.NavigationUtils
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun MyCoinNewsScreen(
    navController: NavHostController,
    viewModel: MyCoinNewsViewModel = hiltViewModel()
) {
    val state = rememberLazyListState()

    val scope = rememberCoroutineScope()

    val selectedCoin by viewModel.selectedCoin.collectAsStateWithLifecycle()
    val filter by viewModel.filter.collectAsStateWithLifecycle()
    val articles by viewModel.articles.collectAsStateWithLifecycle()
    val loading by viewModel.loading.collectAsStateWithLifecycle()
    val watchedNews by viewModel.watchedNewsIds.collectAsStateWithLifecycle()
//    var refreshing by remember { mutableStateOf(false) }
//    val refreshScope = rememberCoroutineScope()
//
//    val pullRefreshState = rememberPullRefreshState(
//        refreshing = refreshing,
//        onRefresh = {
//            refreshScope.launch {
//                refreshing = true
//                // refresh todo
//                delay(2000)
//                refreshing = false
//            }
//        })

    ArticleListScreenContent(
        watchedNewsIds = watchedNews,
        isLoading = loading,
        articles = articles,
        selectedCoin = selectedCoin,
        filter = filter,
        onCoinClick = {
            viewModel.onCoinClick(it)
            scope.launch { state.animateScrollToItem(0) }
        },
        onFilterSettingClick = {
            NavigationUtils.navigate(
                navController,
                CoinListNav.route
            )
        },
        onArticleClick = {
            NavigationUtils.saveArticle(it)
            NavigationUtils.navigate(
                navController,
                ArticleDetailNav.navigateWithArg(it)
            )
            viewModel.onNewsClick(it.id)
        },
//        pullRefreshState = pullRefreshState,
        modifier = Modifier.fillMaxWidth(),
        state = state
    )
}

@Composable
private fun ArticleListScreenContent(
    watchedNewsIds: Set<String>,
    isLoading: Boolean,
    filter: Filter?,
    articles: List<Article>,
    selectedCoin: Coin?,
    onFilterSettingClick: () -> Unit,
    onCoinClick: (Coin) -> Unit,
    onArticleClick: (Article) -> Unit,
//    pullRefreshState: PullRefreshState,
    state: LazyListState,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        if (filter == null || filter.coins.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                EmptyFiltersContent(
                    text = "보고싶은 뉴스의 코인을 선택해보세요!"
                )
                Spacer(modifier = Modifier.height(10.dp))
                ClickableChip(
                    text = "코인 선택",
                    onClick = { onFilterSettingClick() },
                )
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Spacer(modifier = Modifier.height(15.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    LazyRow(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(filter.coins.size) {
                            SelectableChip(
                                selected = filter.coins[it] == selectedCoin,
                                text = filter.coins[it].name,
                                onClick = { onCoinClick(filter.coins[it]) }
                            )
                        }
                    }
                    SettingButton(
                        text = "전체",
                        onClick = { onFilterSettingClick() }
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(
                    thickness = 0.7.dp,
                    color = Grey200,
                )
                if (isLoading) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(38.dp)
                        )
                    }
                }
                LazyColumn(
                    contentPadding = contentPadding,
                    modifier = Modifier.fillMaxSize(),
                    state = state
                ) {
                    items(
                        articles.size,
//                        key = { articles[it].id } todo
                    ) { index ->
                        if (index == 0) {
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                        articles[index].let {
                            ArticleContentItem(
                                watchedNewsIds = watchedNewsIds,
                                article = it,
                                onArticleClick = onArticleClick,
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
private fun ArticleContentItem(
    watchedNewsIds: Set<String>,
    article: Article,
    onArticleClick: (Article) -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val titleColor = if (watchedNewsIds.contains(article.id)) {
        Color(0xFFAAAAAA)
    } else {
        Grey1000
    }

    Column(
        modifier = modifier.clickableWithoutRipple(
            interactionSource = interactionSource,
        ) {
            onArticleClick(article)
        },
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Text(
            text = article.title,
            style = defaultTextStyle.copy(
                fontSize = 16.sp,
                lineHeight = 20.sp,
            ),
            color = titleColor,
            fontWeight = FontWeight.Medium,
            maxLines = 3,
        )
        ArticleMetaData(
            article = article,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun ArticleMetaData(
    article: Article,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(
            text = article.author + "・" + article.createdAt?.let { DateUtils.getTimeAgo(it) },
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
//fun PreviewArticleContent(
//    @PreviewParameter(ArticleContentPreviewParamProvider::class) articles: Flow<PagingData<Article>>
//) {
//    CoinNewsAppTheme {
//        ArticleListScreenContent(
//            articles = articles.collectAsLazyPagingItems(),
//            filters = listOf(
//                CoinFilter("비트코인", "BTC", symbol = ""),
//                CoinFilter("이더리움", "ETC", symbol = "")
//            ),
//            onCoinFilterClick = {},
//            onFilterSettingClick = {},
//            selectedFilter = Filter(CoinFilter("비트코인", "BTC", ""), CountryScope.Local),
//            onArticleClick = {},
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(10.dp)
//                .background(White)
//        )
//    }
//}
//
//private class ArticleContentPreviewParamProvider :
//    PreviewParameterProvider<Flow<PagingData<Article>>> {
//
//    override val values: Sequence<Flow<PagingData<Article>>> =
//        sequenceOf(
//            flowOf(
//                PagingData.from(
//                    listOf(
//                        Article(
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