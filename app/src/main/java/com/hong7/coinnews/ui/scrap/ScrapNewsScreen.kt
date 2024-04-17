package com.hong7.coinnews.ui.scrap

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.hong7.coinnews.R
import com.hong7.coinnews.model.Article
import com.hong7.coinnews.model.Coin
import com.hong7.coinnews.ui.ArticleDetailNav
import com.hong7.coinnews.ui.theme.Blue50
import com.hong7.coinnews.ui.theme.Blue600
import com.hong7.coinnews.ui.theme.Grey1000
import com.hong7.coinnews.ui.theme.Grey200
import com.hong7.coinnews.ui.utils.DateUtils
import com.hong7.coinnews.ui.utils.NavigationUtils

@Composable
fun ScrapNewsScreen(
    navController: NavHostController,
    viewModel: ScrapNewsViewModel = hiltViewModel()
) {
    val news by viewModel.news.collectAsState()

    ScrapNewsScreen(
        news = news,
        onArticleClick = {
            NavigationUtils.saveArticle(it)
            NavigationUtils.navigate(
                navController,
                ArticleDetailNav.navigateWithArg(it)
            )
        },
        onDeleteClick = {},
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
private fun ScrapNewsScreen(
    news: List<Article>,
    onDeleteClick: (coin: Coin) -> Unit,
    onArticleClick: (Article) -> Unit,
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState()
) {
    Scaffold(
        modifier = modifier
    ) { contentPadding ->
        if (news.isEmpty()) {
            EmptyNewsContent(
                text = "스크랩 뉴스가 존재하지 않습니다.",
                modifier = modifier.padding(contentPadding)
            )
        } else {
            LazyColumn(
                contentPadding = contentPadding,
                modifier = modifier.fillMaxWidth(),
                state = state
            ) {
                items(
                    items = news,
                    key = { coin -> coin.id }
                ) {
                    ArticleContentItem(
                        article = it,
                        onArticleClick = onArticleClick,
                        modifier = Modifier.fillMaxWidth()
                    )
                    HorizontalDivider(
                        thickness = 0.7.dp,
                        color = Grey200,
                        modifier = Modifier.padding(vertical = 15.dp)
                    )
                }
            }
        }
        Column(
            modifier = modifier.fillMaxWidth()
                .height(100.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom,
        ) {
            Text(
                text = "문의 Email : ghdrl7526@gmail.com",
                style = MaterialTheme.typography.bodyLarge,
                color = Blue600,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun EmptyNewsContent(
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
            fontWeight = FontWeight.Medium,
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
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
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





