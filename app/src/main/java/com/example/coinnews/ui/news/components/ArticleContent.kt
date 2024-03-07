package com.example.coinnews.ui.news.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.coinnews.model.Article
import com.example.coinnews.model.ArticleMetaData
import com.example.coinnews.ui.theme.CoinNewsAppTheme
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

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
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.Normal,
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
            text = article.metaData.author ?: "알 수 없는 출처",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Normal
        )
        Text(
            text = "・",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Normal
        )
        Text(
            text = getTimeAgo(article.metaData.createdAt, LocalDateTime.now()),
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
            url = "url",
            description = "한편, 한화투자증권은 2021년 2월에 암호화폐 거래소 업비트와 주식 거래 플랫폼 증권플러스 등을 운영하는 두나무 보통주 약 200만주를 583억원에 매수한 바 있다.",
            metaData = ArticleMetaData(
                author = "블록미디어",
                createdAt = LocalDateTime.now()
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

private fun getTimeAgo(fromTime: LocalDateTime, toTime: LocalDateTime = LocalDateTime.now()): String {
    val minutes = ChronoUnit.MINUTES.between(fromTime, toTime)
    val hours = ChronoUnit.HOURS.between(fromTime, toTime)
    val days = ChronoUnit.DAYS.between(fromTime, toTime)

    return when {
        minutes < 60 -> "$minutes 분 전"
        hours < 24 -> "$hours 시간 전"
        else -> "$days 일 전"
    }
}