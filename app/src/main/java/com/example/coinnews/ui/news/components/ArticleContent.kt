package com.example.coinnews.ui.news.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.coinnews.model.Article
import com.example.coinnews.model.ArticleAsset
import com.example.coinnews.model.ArticleMetaData
import com.example.coinnews.ui.theme.CoinNewsAppTheme
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun ArticleContent(
    articles: LazyPagingItems<Article>,
    onArticleClick: (Article) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    state: LazyListState = rememberLazyListState()
) {
    // todo add empty state
    LazyColumn(
        contentPadding = contentPadding,
        modifier = modifier,
        state = state
    ) {
        items(articles.itemCount) { index ->
            articles[index]?.let{
                ArticleContentItem(
                    article = it,
                    onArticleClick = onArticleClick,
                    modifier = Modifier
                )
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
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Column(
                modifier = Modifier.weight(1F),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.Start,
            ) {
                Text(
                    text = article.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
                ArticleMetaData(
                    article = article,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            AsyncImage(
                model = article.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .width(110.dp)
                    .height(104.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
        }
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
        article.assets.forEach { asset ->
            Text(
                text = asset.symbol,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Normal
            )
        }
        Text(
            text = "・",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Normal
        )
        Text(
            text = article.metaData.author,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Normal
        )
        Text(
            text = "・",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Normal
        )
        Text(
            text = article.metaData.createdAt,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Normal
        )
    }
}

@Preview
@Composable
fun PreviewArticleContent() {
    val items = listOf(
        Article(
            id = "1",
            title = "블롬버그 \"버블 조짐 있다\"vs 월스트리트저널 \"과거 만큼은 아니다\"",
            imageUrl = "imageUrl",
            url = "url",
            assets = listOf(
                ArticleAsset(
                    id = "1",
                    name = "ether",
                    symbol = "eth"
                )
            ),
            metaData = ArticleMetaData(
                author = "블록미디어",
                createdAt = "2시간 전"
            )
        )
    )

    val flow = MutableStateFlow(PagingData.from(items))

    CoinNewsAppTheme {
        ArticleContent(
            articles = flow.collectAsLazyPagingItems(),
            onArticleClick = {},
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
                .background(White)
        )
    }
}
