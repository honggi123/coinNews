package com.hong7.coinnews.ui.articledetail

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.perf.FirebasePerformance
import com.google.firebase.perf.metrics.Trace
import com.hong7.coinnews.R
import com.hong7.coinnews.model.Article
import com.hong7.coinnews.model.ArticleWithInterest
import com.hong7.coinnews.ui.extensions.clickableWithoutRipple
import com.hong7.coinnews.ui.theme.Blue600
import com.hong7.coinnews.ui.theme.CoinNewsAppTheme
import com.hong7.coinnews.ui.theme.Grey100
import com.hong7.coinnews.ui.theme.Grey200
import com.hong7.coinnews.ui.theme.Grey500
import com.hong7.coinnews.ui.theme.Grey700
import com.hong7.coinnews.ui.theme.defaultTextStyle
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer
import com.valentinilk.shimmer.unclippedBoundsInWindow
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
            Column(modifier = Modifier.fillMaxWidth()) {
                TopAppBar(
                    article = article,
                    isInterested = isInterested,
                    onBackClick = onBackClick,
                    onToggleClick = onToggleClick,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
    ) { paddingValues ->
        if (article != null) {
            ArticleContent(
                url = article.url,
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
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(horizontal = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_cancel),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxHeight()
                    .clickableWithoutRipple(
                        interactionSource = interactionSource,
                    ) { onBackClick() },
                tint = Grey700
            )
            Spacer(modifier = Modifier.width(24.dp))
            Row(
                modifier = Modifier
                    .weight(1f)
                    .background(
                        color = Grey100,
                        shape = RoundedCornerShape(6.dp),
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = article?.url ?: "",
                    color = Grey500,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Normal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                )
            }
            Spacer(modifier = Modifier.width(24.dp))
            Icon(
                painter = interestIconPainter,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxHeight()
                    .clickableWithoutRipple(
                        interactionSource = interactionSource,
                    ) {
                        if (article != null) {
                            onToggleClick(
                                ArticleWithInterest(article, isInterested)
                            )
                        }
                    },
                tint = Grey700
            )
        }

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
    url: String,
    modifier: Modifier = Modifier
) {
    var isLoading by rememberSaveable { mutableStateOf(true) }
    val trace: Trace =
        FirebasePerformance.getInstance().newTrace("article_page_loaded")

    Box(
        modifier = modifier,
    ) {
        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    webViewClient = object : WebViewClient() {
                        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                            super.onPageStarted(view, url, favicon)
                            isLoading = true
                            trace.start()
                        }

                        override fun onPageFinished(view: WebView?, url: String?) {
                            super.onPageFinished(view, url)
                            isLoading = false
                            trace.stop()
                        }
                    }
                    settings.javaScriptEnabled = true
                    settings.javaScriptCanOpenWindowsAutomatically = false

                    loadUrl(url)
                }
            },
            modifier = Modifier.fillMaxSize()
        )
        if (isLoading) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(White)
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .shimmer()
                        .background(Grey500)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp)
                        .shimmer()
                        .background(Grey500)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .shimmer()
                        .background(Grey500),
                )
                repeat(4){
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(30.dp)
                            .shimmer()
                            .background(Grey500)
                    )
                    Box(
                        modifier = Modifier
                            .width(220.dp)
                            .height(30.dp)
                            .shimmer()
                            .background(Grey500)
                    )
                    Box(
                        modifier = Modifier
                            .width(300.dp)
                            .height(30.dp)
                            .shimmer()
                            .background(Grey500)
                    )
                }
            }
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

