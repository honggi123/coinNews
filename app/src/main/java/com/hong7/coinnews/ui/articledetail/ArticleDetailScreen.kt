package com.hong7.coinnews.ui.articledetail

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.perf.FirebasePerformance
import com.google.firebase.perf.metrics.Trace
import com.hong7.coinnews.R
import com.hong7.coinnews.model.Article
import com.hong7.coinnews.model.ArticleWithInterest
import com.hong7.coinnews.ui.theme.CoinNewsAppTheme
import com.hong7.coinnews.ui.theme.Grey700
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
                text = "해당 뉴스가 존재하지 않습니다.",
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
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_cancel),
                contentDescription = null,
                modifier = Modifier
                    .size(26.dp)
                    .clickable { onBackClick() },
                tint = Grey700
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
                tint = Grey700
            )
        }
        HorizontalDivider()
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

@SuppressLint("SetJavaScriptEnabled")
@Composable
private fun ArticleContent(
    article: Article,
    modifier: Modifier = Modifier
) {
    var isLoading by rememberSaveable { mutableStateOf(true) }
    val trace: Trace =
        FirebasePerformance.getInstance().newTrace("article_page_loaded")

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    webViewClient = object : WebViewClient() {
                        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                            super.onPageStarted(view, url, favicon)
                            trace.start()
                        }
                        override fun onPageFinished(view: WebView?, url: String?) {
                            super.onPageFinished(view, url)
                            isLoading = false
                            trace.stop()
                        }
                    }
                    settings.javaScriptEnabled = true
                    loadUrl(article.url)
                }
            },
            modifier = Modifier.fillMaxSize()
        )
        if (isLoading) {
            CircularProgressIndicator()
        }
    }
}

@Preview
@Composable
private fun ArticleDetailScreenPreview() {
    CoinNewsAppTheme {
        ArticleDetailScreen(
            article = Article(
                id = "",
                title = "",
                description = "",
                url = "",
                author = "",
                createdAt = LocalDateTime.now()
            ),
            isInterested = false,
            onBackClick = {},
            onToggleClick = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}

