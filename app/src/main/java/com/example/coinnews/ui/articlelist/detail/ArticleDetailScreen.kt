package com.example.coinnews.ui.articlelist.detail

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.coinnews.R
import com.example.coinnews.model.Article
import com.example.coinnews.model.ArticleWithInterest
import com.example.coinnews.model.CoinAsset
import com.example.coinnews.model.Coin
import com.example.coinnews.ui.theme.CoinNewsAppTheme
import com.example.coinnews.ui.theme.Grey1000
import com.example.coinnews.ui.theme.Grey200
import java.time.Instant
import java.time.LocalDateTime

@Composable
fun ArticleDetailRoute(
    article: Article?,
    onBackClick: () -> Unit,
    viewModel: ArticleDetailViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.updateArticle(article)
    }

    val isInterested by viewModel.isInterested.collectAsState()

    ArticleDetailScreen(
        article = article,
        isInterested = isInterested,
        onBackClick = onBackClick,
        onToggleClick = viewModel::toggleInterest,
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
private fun ArticleDetailScreen(
    article: Article?,
    isInterested: Boolean,
    onBackClick: () -> Unit,
    onToggleClick: (ArticleWithInterest) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                article = article,
                isInterested = isInterested,
                onBackClick = onBackClick,
                onToggleClick = onToggleClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            )
        }
    ) { paddingValues ->
        if (article != null) {
            ArticleDetailContent(
                article = article,
                modifier = modifier
                    .padding(paddingValues)
            )
        } else {
            EmptyArticleContent(
                text = "해당 뉴스의 URL이 존재하지 않습니다.",
                modifier = modifier
                    .padding(paddingValues)
            )
        }
    }
}

@Composable
private fun TopAppBar(
    article: Article?,
    isInterested: Boolean,
    onBackClick: () -> Unit,
    onToggleClick: (ArticleWithInterest) -> Unit,
    modifier: Modifier = Modifier
) {
    val interestIconPainter = if (isInterested) {
        painterResource(id = R.drawable.ic_star_fill)
    } else {
        painterResource(id = R.drawable.ic_star_border)
    }

    Row(
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_arrow_back),
            contentDescription = null,
            modifier = Modifier
                .size(30.dp)
                .clickable { onBackClick() },
            tint = Grey1000
        )
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            painter = interestIconPainter,
            contentDescription = null,
            modifier = Modifier
                .size(30.dp)
                .clickable {
                    if (article != null) {
                        onToggleClick(
                            ArticleWithInterest(article, isInterested)
                        )
                    }
                },
            tint = Grey1000
        )
    }
}

@Composable
private fun ArticleDetailContent(
    article: Article,
    modifier: Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        ArticleContent(
            article = article,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun EmptyArticleContent(
    text: String,
    modifier: Modifier
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
private fun ArticleContent(
    article: Article,
    modifier: Modifier = Modifier
) {
    AndroidView(
        factory = { context ->
            WebView(context).apply {
                webViewClient = WebViewClient()

                settings.loadWithOverviewMode = true
                settings.useWideViewPort = true
                settings.setSupportZoom(true)
            }
        },
        update = { webView ->
            webView.loadUrl(article.url)
        },
        modifier = modifier
    )
}

@Preview
@Composable
private fun ArticleDetailScreenPreview() {
    CoinNewsAppTheme {
        ArticleDetailScreen(
            article = Article(
                id = "",
                title = "",
//                description = "",
                url = "",
                author = "",
                createdAt = Instant.now().toEpochMilli()
            ),
            isInterested = false,
            onBackClick = {},
            onToggleClick = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}

