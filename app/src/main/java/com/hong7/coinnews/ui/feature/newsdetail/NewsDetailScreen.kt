package com.hong7.coinnews.ui.feature.newsdetail

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.firebase.perf.FirebasePerformance
import com.google.firebase.perf.metrics.Trace
import com.hong7.coinnews.R
import com.hong7.coinnews.model.News
import com.hong7.coinnews.model.NewsWithInterest
import com.hong7.coinnews.ui.extensions.clickableWithoutRipple
import com.hong7.coinnews.ui.theme.CoinNewsAppTheme
import com.hong7.coinnews.ui.theme.Grey100
import com.hong7.coinnews.ui.theme.Grey500
import com.hong7.coinnews.ui.theme.Grey700
import com.valentinilk.shimmer.shimmer
import java.time.LocalDateTime


@Composable
fun NewsDetailRoute(
    onBackClick: () -> Unit,
    viewModel: NewsDetailViewModel = hiltViewModel()
) {
    val isScraped by viewModel.isScraped.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (val state = uiState) {
        is NewsDetailUiState.Success -> {
            NewsDetailScreen(
                news = state.news,
                isScraped = isScraped,
                onBackClick = onBackClick,
                onToggleClick = viewModel::onToggleClick,
                modifier = Modifier.fillMaxSize()
            )
        }
        is NewsDetailUiState.Failed, is NewsDetailUiState.Loading  -> {} // todo
    }
}

@Composable
private fun NewsDetailScreen(
    news: News?,
    isScraped: Boolean,
    onBackClick: () -> Unit,
    onToggleClick: (NewsWithInterest) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            Column(modifier = Modifier.fillMaxWidth()) {
                TopAppBar(
                    news = news,
                    isScraped = isScraped,
                    onBackClick = onBackClick,
                    onToggleClick = onToggleClick,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
    ) { paddingValues ->
        if (news != null) {
            NewsContent(
                url = news.url,
                date = LocalDateTime.now(),
                modifier = modifier
                    .padding(paddingValues)
            )
        } else {
            EmptyNewsContent(
                text = "해당 뉴스가 존재하지 않습니다.",
                modifier = modifier
                    .padding(paddingValues)
            )
        }
    }
}

@Composable
private fun TopAppBar(
    news: News?,
    isScraped: Boolean,
    onBackClick: () -> Unit,
    onToggleClick: (NewsWithInterest) -> Unit,
    modifier: Modifier = Modifier
) {
    val interestIconPainter = if (isScraped) {
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
                    text = news?.url ?: "",
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
                        if (news?.url != null) {
                            onToggleClick(
                                NewsWithInterest(news, isScraped)
                            )
                        }
                    },
                tint = Grey700
            )
        }

    }
}

@Composable
private fun EmptyNewsContent(
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
private fun NewsContent(
    url: String?,
    date: LocalDateTime,
    modifier: Modifier = Modifier
) {
    var isLoading by rememberSaveable { mutableStateOf(true) }
    val trace: Trace =
        FirebasePerformance.getInstance().newTrace("news_page_loaded")

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

                        @Deprecated("Deprecated in Java")
                        override fun shouldOverrideUrlLoading(
                            view: WebView?,
                            url: String?
                        ): Boolean {
                            val host = Uri.parse(url).getHost();
                            if (url == null) {
                                return true
                            }
                            if (!TextUtils.isEmpty(host) && url.startsWith("fb://") && host?.contains(
                                    "profile"
                                ) == true
                            ) {
                                return true
                            } else {
                                loadUrl(url)
                            }
                            return true
                        }
                    }
                    settings.javaScriptEnabled = true
//                    settings.javaScriptCanOpenWindowsAutomatically = false
                    loadUrl(url.toString())
                }
            },
            modifier = Modifier.fillMaxSize()
        )
//        if (isLoading) {
//            LoadingContent()
//        }
    }
}

@Composable
private fun LoadingContent(){
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
        repeat(4) {
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

@Preview
@Composable
private fun NewsDetailScreenPreview() {
    CoinNewsAppTheme {
        NewsDetailScreen(
            news = null,
            isScraped = false,
            onBackClick = {},
            onToggleClick = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}

