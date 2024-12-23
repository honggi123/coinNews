package com.hong7.coinnews.ui.feature.newslist

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.hong7.coinnews.model.News
import com.hong7.coinnews.ui.component.ClickableChip
import com.hong7.coinnews.ui.extensions.clickableWithoutRipple
import com.hong7.coinnews.ui.theme.Blue600
import com.hong7.coinnews.ui.theme.Grey1000
import com.hong7.coinnews.ui.theme.Grey200
import com.hong7.coinnews.ui.theme.Grey500
import com.hong7.coinnews.ui.theme.coinNewsTypography
import com.hong7.coinnews.ui.theme.defaultTextStyle
import com.hong7.coinnews.utils.DateUtils


@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun NewsListScreen(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    viewModel: NewsViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val uistate by viewModel.uiState.collectAsStateWithLifecycle()
    val watchedNews by viewModel.watchedNewsIds.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.messageEvent.collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    when (val state = uistate) {
        is NewsScreenUiState.Success -> {
            NewsListScreenContent(
                watchedNewsIds = watchedNews,
                isNewsLoading = state.isNewsLoading,
                newsList = state.newsList,
                onNewsClick = { newsItem ->
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(newsItem.url))
                    context.startActivity(intent)
                    viewModel.onNewsClick(newsItem.id)
                },
                onBackClick = {
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth(),
            )

        }

        is NewsScreenUiState.Failed -> {
            LoadingContent(
                modifier = Modifier.fillMaxSize()
            )
        }

        else -> Unit
    }
}

@Composable
private fun EmptyFilterContent(
    onFilterSettingClick: () -> Unit,
    modifier: Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EmptyFiltersContent(
            text = "Select the coins for the news you want to see!"
        )
        Spacer(modifier = Modifier.height(10.dp))
        ClickableChip(
            text = "Select Coins",
            onClick = { onFilterSettingClick() },
        )
    }
}

@Composable
private fun LoadingContent(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(38.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NewsListScreenContent(
    watchedNewsIds: Set<String>,
    isNewsLoading: Boolean,
    newsList: List<News>?,
    onNewsClick: (News) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val state = rememberLazyListState()

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        rememberTopAppBarState()
    )

    Scaffold(
        modifier = modifier
    ) { contentPadding ->
        Column(
            modifier = Modifier.padding(contentPadding),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            HorizontalDivider(
                thickness = 0.7.dp,
                color = Grey200,
            )
            if (isNewsLoading) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(38.dp)
                    )
                }
            } else {
                NewsList(
                    watchedNewsIds = watchedNewsIds,
                    newsList = newsList ?: emptyList(),
                    onNewsClick = onNewsClick,
                    modifier = Modifier.fillMaxWidth(),
                    state = state
                )
            }
        }
    }
}

@Composable
private fun NewsList(
    watchedNewsIds: Set<String>,
    newsList: List<News>,
    onNewsClick: (News) -> Unit,
    state: LazyListState,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {

    LazyColumn(
        contentPadding = contentPadding,
        modifier = Modifier.fillMaxSize(),
        state = state
    ) {
        items(
            newsList.size,
//                        key = { newss[it].id } todo
        ) { index ->
            if (index == 0) {
                Spacer(modifier = Modifier.height(10.dp))
            }
            newsList[index].let {
                NewsContentItem(
                    watchedNewsIds = watchedNewsIds,
                    news = it,
                    onNewsClick = onNewsClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                )
                HorizontalDivider(
                    thickness = 0.7.dp,
                    color = Grey200,
                    modifier = Modifier.padding(vertical = 15.dp)
                )
            }
        }

    }
}

@Composable
private fun SettingButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 5.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Blue600,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun EmptyFiltersContent(
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
            style = defaultTextStyle.copy(
                fontSize = 16.sp,
                lineHeight = 20.sp,
            ),
            fontWeight = FontWeight.Medium,
        )
    }
}

@Composable
private fun NewsContentItem(
    watchedNewsIds: Set<String>,
    news: News,
    onNewsClick: (News) -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val titleColor = if (watchedNewsIds.contains(news.id)) {
        Color(0xFFAAAAAA)
    } else {
        Grey1000
    }

    Column(
        modifier = modifier.clickableWithoutRipple(
            interactionSource = interactionSource,
        ) {
            onNewsClick(news)
        },
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = news.title,
            style = coinNewsTypography.titleMedium.copy(
                color = titleColor,
                fontWeight = FontWeight.Bold
            ),
            maxLines = 3,
        )
        NewsMetaData(
            news = news,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun NewsMetaData(
    news: News,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(
            text = news.createdAt?.let { DateUtils.getTimeAgo(it) }.toString(),
            style = coinNewsTypography.bodySmall.copy(
                fontWeight = FontWeight.Medium
            ),
            color = Grey500
        )
    }
}
