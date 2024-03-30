package com.example.coinnews.ui.scrap

import androidx.compose.foundation.Image
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.coinnews.R
import com.example.coinnews.model.Article
import com.example.coinnews.model.Coin
import com.example.coinnews.ui.utils.DateUtils

@Composable
fun ScrapNewsScreen(
    viewModel: ScrapNewsViewModel = hiltViewModel()
) {
    val news by viewModel.news.collectAsState()

    ScrapNewsScreen(
        news = news,
        onDeleteClick = {},
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
private fun ScrapNewsScreen(
    news: List<Article>,
    onDeleteClick: (coin: Coin) -> Unit,
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState()
) {
    Scaffold(
        modifier = modifier
    ) { contentPadding ->
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
                    onArticleClick = {},
                )
                Spacer(modifier = Modifier.height(30.dp))
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
//        Text(
//            text = article.description,
//            style = MaterialTheme.typography.bodyLarge,
//            fontWeight = FontWeight.Medium,
//        )
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
            text = DateUtils.timestampToAmPmTimeString(article.createdAt),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Normal
        )
    }
}





