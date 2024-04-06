package com.hong7.coinnews.ui.articlelist

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.hong7.coinnews.model.Article
import com.hong7.coinnews.model.CoinFilter
import com.hong7.coinnews.model.CountryScope
import com.hong7.coinnews.model.Filter
import com.hong7.coinnews.ui.ArticleDetailNav
import com.hong7.coinnews.ui.component.BaseCustomModal
import com.hong7.coinnews.ui.component.CheckListItem
import com.hong7.coinnews.ui.component.ClickableChip
import com.hong7.coinnews.ui.component.SelectableChip
import com.hong7.coinnews.ui.theme.Blue600
import com.hong7.coinnews.ui.theme.Grey200
import com.hong7.coinnews.ui.theme.GreyOpacity400
import com.hong7.coinnews.ui.utils.DateUtils
import com.hong7.coinnews.ui.utils.NavigationUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun ArticleListScreen(
    navController: NavHostController,
    viewModel: ArticleListViewModel = hiltViewModel()
) {
    val sheetState = rememberModalBottomSheetState(true)
    val state = rememberLazyListState()
    var showModal by rememberSaveable { mutableStateOf(false) }

    val selectedFilter by viewModel.selectedCoinFilter.collectAsStateWithLifecycle()
    val userFilter by viewModel.userFilter.collectAsStateWithLifecycle()
    val allFilters by viewModel.allFilters.collectAsStateWithLifecycle()

    val articles = viewModel.articles.collectAsLazyPagingItems()

    ArticleListScreenContent(
        articles = articles,
        selectedFilter = selectedFilter,
        coinFilters = userFilter?.coinFilters ?: emptyList(), // todo
        onCoinFilterClick = viewModel::onCoinFilterClick,
        onFilterSettingClick = { showModal = true },
        onArticleClick = {
            NavigationUtils.saveArticle(it)
            NavigationUtils.navigate(
                navController,
                ArticleDetailNav.navigateWithArg(it)
            )
        },
        modifier = Modifier.fillMaxWidth(),
        state = state
    )
    if (showModal && allFilters != null) {
        CoinFilterBottomModal(
            filter = allFilters!!,
            sheetState = sheetState,
            onCloseClick = { showModal = false },
            onCompleteClick = { filter ->
                viewModel.saveCoinFilters(filter)
                showModal = false
            }
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ArticleListScreenContent(
    articles: LazyPagingItems<Article>,
    selectedFilter: CoinFilter?,
    coinFilters: List<CoinFilter>,
    onFilterSettingClick: () -> Unit,
    onCoinFilterClick: (CoinFilter) -> Unit,
    onArticleClick: (Article) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    state: LazyListState
) {
    var refreshing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val pullRefreshState = rememberPullRefreshState(
        refreshing = refreshing,
        onRefresh = {
            refreshing = true
            articles.refresh()
            scope.launch {
                delay(5000)
            }
            refreshing = false
        })

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        if (coinFilters.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                EmptyFiltersContent(text = "보고싶은 뉴스의 코인을 선택해보세요!")
                Spacer(modifier = Modifier.height(10.dp))
                ClickableChip(
                    text = "필터링 설정",
                    onClick = { onFilterSettingClick() },
                )
            }
        } else {
            val pullRefreshModifier = Modifier.pullRefresh(pullRefreshState)

            Box(
                modifier = pullRefreshModifier,
                contentAlignment = Alignment.Center
            ) {
                LazyColumn(
                    contentPadding = contentPadding,
                    modifier = Modifier.fillMaxSize(),
                    state = state
                ) {
                    item {
                        Spacer(modifier = Modifier.height(15.dp))
                        LazyRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(coinFilters.size) {
                                SelectableChip(
                                    selected = coinFilters[it] == selectedFilter,
                                    text = coinFilters[it].coinName,
                                    onClick = { onCoinFilterClick(coinFilters[it]) }
                                )
                            }
                            item {
                                SettingButton(
                                    text = "설정",
                                    onClick = { onFilterSettingClick() }
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(15.dp))
                        HorizontalDivider(
                            thickness = 0.7.dp,
                            color = Grey200
                        )
                        Spacer(modifier = Modifier.height(15.dp))
                    }
                    items(articles.itemCount) { index ->
                        articles[index]?.let {
                            ArticleContentItem(
                                article = it,
                                onArticleClick = onArticleClick,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(15.dp))
                            HorizontalDivider(
                                thickness = 0.7.dp,
                                color = Grey200
                            )
                            Spacer(modifier = Modifier.height(15.dp))
                        }
                    }
                    item {
                        if (selectedFilter == null || !coinFilters.contains(selectedFilter)) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "보고싶은 뉴스의 코인을 선택하세요!",
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Bold,
                                )
                            }
                        }
                    }
                }
                PullRefreshIndicator(
                    refreshing = refreshing,
                    state = pullRefreshState,
                    modifier = Modifier.align(Alignment.TopCenter)
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
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
private fun ArticleContentItem(
    article: Article,
    onArticleClick: (Article) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.clickable { onArticleClick(article) },
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Text(
            text = article.title,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            maxLines = 2,
            softWrap = false
        )
        Text(
            text = article.description,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            maxLines = 2,
            softWrap = false
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
            text = article.author ?: "알 수 없는 출처",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Normal
        )
        Text(
            text = "・",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Normal
        )
        Text(
            text = article.createdAt?.let { DateUtils.getTimeAgo(it) } ?: "",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Normal
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
private fun CoinFilterBottomModal(
    filter: Filter,
    onCloseClick: () -> Unit,
    onCompleteClick: (filter: Filter) -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(),
) {
    val filters = rememberSaveable { mutableStateOf(filter.coinFilters) }
    val scope = rememberSaveable { mutableStateOf(filter.scope) }

    ModalBottomSheet(
        modifier = Modifier.padding(horizontal = 10.dp),
        sheetState = sheetState,
        tonalElevation = 0.dp,
        scrimColor = GreyOpacity400,
        onDismissRequest = { onCloseClick() },
        dragHandle = null,
        containerColor = Color.Transparent,
    ) {
        BoxWithConstraints(
            Modifier
                .navigationBarsPadding()
                .padding(bottom = 30.dp)
        ) {
            BaseCustomModal(
                onDismissClick = onCloseClick,
                actionButtonText = "등록",
                onActionClick = {
                    onCompleteClick(
                        filter.copy(
                            coinFilters = filters.value,
                            scope = scope.value
                        )
                    )
                },
                modifier = modifier,
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    CheckListItem(
                        checked = scope.value != CountryScope.Local,
                        text = "해외 뉴스",
                        onClick = {
                            scope.value = if (it) {
                                CountryScope.Global
                            } else {
                                CountryScope.Local
                            }
                        }
                    )
                    HorizontalDivider()
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        filters.value.forEachIndexed { index, coinFilter ->
                            CheckListItem(
                                checked = coinFilter.isSelected,
                                text = coinFilter.coinName,
                                onClick = {
                                    val newFilters = filters.value.toMutableList()
                                    newFilters[index] = coinFilter.copy(isSelected = it)
                                    filters.value = newFilters
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                            )
                        }
                    }
                }
            }
        }
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
