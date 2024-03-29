package com.example.coinnews.ui.articlelist

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.coinnews.model.Article
import com.example.coinnews.model.ArticleMetaData
import com.example.coinnews.model.CoinFilter
import com.example.coinnews.ui.component.BaseCustomModal
import com.example.coinnews.ui.component.CheckListItem
import com.example.coinnews.ui.component.ClickableChip
import com.example.coinnews.ui.component.SelectableChip
import com.example.coinnews.ui.theme.CoinNewsAppTheme
import com.example.coinnews.ui.theme.Grey1000
import com.example.coinnews.ui.theme.Grey200
import com.example.coinnews.ui.theme.Grey700
import com.example.coinnews.ui.theme.GreyOpacity400
import com.example.coinnews.ui.utils.DateUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun ArticleListScreen(
    onArticleClick: (Article) -> Unit,
    viewModel: ArticleListViewModel = hiltViewModel()
) {
    val sheetState = rememberModalBottomSheetState()
    var showModal by rememberSaveable { mutableStateOf(false) }

    val selectedFilter by viewModel.selectedFilter.collectAsState()
    val userFilters by viewModel.userFilters.collectAsState()
    val allFilters by viewModel.allFilters.collectAsState()

    val articles = viewModel.articles.collectAsLazyPagingItems()

    ArticleListScreenContent(
        articles = articles,
        selectedFilter = selectedFilter,
        filters = userFilters,
        onFilterClick = viewModel::onFilterClick,
        onFilterSettingClick = { showModal = true },
        onArticleClick = onArticleClick,
        modifier = Modifier.fillMaxWidth()
    )
    if (showModal) {
        CoinFilterBottomModal(
            sheetState = sheetState,
            allCoinFilters = allFilters,
            onCloseClick = { showModal = false },
            onCompleteClick = {
                viewModel.updateNewFilters(it)
                showModal = false
            },
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ArticleListScreenContent(
    articles: LazyPagingItems<Article>,
    selectedFilter: CoinFilter?,
    filters: List<CoinFilter>,
    onFilterSettingClick: () -> Unit,
    onFilterClick: (CoinFilter) -> Unit,
    onArticleClick: (Article) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    state: LazyListState = rememberLazyListState()
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            filters.forEach {
                SelectableChip(
                    selected = it == selectedFilter,
                    text = it.coinName,
                    onClick = { onFilterClick(it) }
                )
            }
            ClickableChip(
                text = "설정",
                onClick = { onFilterSettingClick() },
            )
        }
        LazyColumn(
            contentPadding = contentPadding,
            modifier = Modifier.fillMaxSize(),
            state = state
        ) {
            items(articles.itemCount) { index ->
                articles[index]?.let {
                    ArticleContentItem(
                        article = it,
                        onArticleClick = onArticleClick,
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
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
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            text = article.title,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = article.description,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
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
            text = article.metaData?.author ?: "알 수 없는 출처",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Normal
        )
        Text(
            text = "・",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Normal
        )
        article.metaData?.let {
            Text(
                text = DateUtils.timeToHourMinString(article.metaData.createdAt) ?: "", // todo
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Normal
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
private fun CoinFilterBottomModal(
    allCoinFilters: List<CoinFilter>,
    onCloseClick: () -> Unit,
    onCompleteClick: (List<CoinFilter>) -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(),
) {
    val filters = rememberSaveable { mutableStateOf(allCoinFilters) }

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
                .padding(bottom = 10.dp)
        ) {
            BaseCustomModal(
                onDismissClick = onCloseClick,
                actionButtonText = "등록",
                onActionClick = { onCompleteClick(filters.value) },
                modifier = modifier,
            ) {
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

@Preview
@Composable
fun PreviewArticleContent(
    @PreviewParameter(ArticleContentPreviewParamProvider::class) articles: Flow<PagingData<Article>>
) {
    CoinNewsAppTheme {
        ArticleListScreenContent(
            articles = articles.collectAsLazyPagingItems(),
            filters = listOf(
                CoinFilter("비트코인", "BTC", symbol = ""),
                CoinFilter("이더리움", "ETC", symbol = "")
            ),
            onFilterClick = {},
            onFilterSettingClick = {},
            selectedFilter = CoinFilter("비트코인", "BTC", ""),
            onArticleClick = {},
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
                .background(White)
        )
    }
}

private class ArticleContentPreviewParamProvider :
    PreviewParameterProvider<Flow<PagingData<Article>>> {

    override val values: Sequence<Flow<PagingData<Article>>> =
        sequenceOf(
            flowOf(
                PagingData.from(
                    listOf(
                        Article(
                            id = "1",
                            title = "블롬버그 \"버블 조짐 있다\"vs 월스트리트저널 \"과거 만큼은 아니다\"",
                            url = "url",
                            description = "한편, 한화투자증권은 2021년 2월에 암호화폐 거래소 업비트와 주식 거래 플랫폼 증권플러스 등을 운영하는 두나무 보통주 약 200만주를 583억원에 매수한 바 있다.",
                            metaData = ArticleMetaData(
                                author = "블록미디어",
                                createdAt = LocalDateTime.now()
                            )
                        )
                    )
                )
            )
        )
}
