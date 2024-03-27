package com.example.coinnews.ui.articlelist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.coinnews.model.Article
import com.example.coinnews.model.ArticleMetaData
import com.example.coinnews.model.CoinFilter
import com.example.coinnews.ui.components.SelectableChip
import com.example.coinnews.ui.theme.CoinNewsAppTheme
import com.example.coinnews.ui.utils.DateUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Composable
fun ArticleListScreen(
    onArticleClick: (Article) -> Unit,
    viewModel: ArticleListViewModel = hiltViewModel()
) {
    val articles = viewModel.articles.collectAsLazyPagingItems()
    val selectedFilter by viewModel.selectedCoinFilter.collectAsStateWithLifecycle()

    val coinFilters = listOf(
        CoinFilter.Bitcoin,
        CoinFilter.Ethereum
    )

    ArticleListScreen(
        articles = articles,
        selectedFilter = selectedFilter,
        filters = coinFilters,
        onFilterClick = viewModel::updateFilter,
        onArticleClick = onArticleClick
    )
}

@Composable
fun ArticleListScreen(
    articles: LazyPagingItems<Article>,
    selectedFilter: CoinFilter?,
    filters: List<CoinFilter>,
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
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            filters.forEach {
                SelectableChip(
                    selected = it == selectedFilter,
                    text = it.coinName,
                    onClick = { onFilterClick(it) }
                )
            }
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

@Preview
@Composable
fun PreviewArticleContent(
    @PreviewParameter(ArticleContentPreviewParamProvider::class) articles: Flow<PagingData<Article>>
) {
    CoinNewsAppTheme {
//        ArticleListScreen(
//            articles = articles.collectAsLazyPagingItems(),
//            filters = listOf(
//                CoinFilter.Bitcoin,
//                CoinFilter.Ethereum
//            ),
//            selectedFilter = CoinFilter.Bitcoin,
//            onArticleClick = {},
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(10.dp)
//                .background(White)
//        )
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
