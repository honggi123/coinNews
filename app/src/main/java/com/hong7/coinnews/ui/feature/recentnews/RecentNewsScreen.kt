package com.hong7.coinnews.ui.feature.recentnews

import android.annotation.SuppressLint
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.hong7.coinnews.model.Article
import com.hong7.coinnews.model.NetworkState
import com.hong7.coinnews.ui.NewsDetailNav
import com.hong7.coinnews.ui.extensions.clickableWithoutRipple
import com.hong7.coinnews.ui.theme.Grey1000
import com.hong7.coinnews.ui.theme.Grey200
import com.hong7.coinnews.ui.theme.defaultTextStyle
import com.hong7.coinnews.utils.DateUtils
import com.hong7.coinnews.utils.NavigationUtils

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun RecentNewsScreen(
    networkState: NetworkState,
    navController: NavHostController,
    viewModel: RecentNewsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val watchedNews by viewModel.watchedNewsIds.collectAsStateWithLifecycle()

    when(val state = uiState){
        is RecentCoinNewsUiState.Loading -> {
            LoadingContent(modifier = Modifier.fillMaxSize())
        }
        is RecentCoinNewsUiState.Success -> {
            ArticleListScreenContent(
                watchedNewsIds = watchedNews,
                articles = state.newsList,
                onArticleClick = {
                    NavigationUtils.navigate(
                        navController,
                        NewsDetailNav.navigateWithArg(it)
                    )
                    viewModel.onNewsClick(it.id)
                },
                modifier = Modifier.fillMaxWidth(),
                state = rememberLazyListState()
            )
        }
        else -> {
            // TODO
        }
    }
}

@Composable
private fun LoadingContent(
    modifier: Modifier = Modifier
){
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

@Composable
private fun ArticleListScreenContent(
    watchedNewsIds: Set<String>,
    articles: List<Article>,
    onArticleClick: (Article) -> Unit,
    state: LazyListState,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            HorizontalDivider(
                thickness = 0.7.dp,
                color = Grey200,
            )
            LazyColumn(
                contentPadding = contentPadding,
                modifier = Modifier.fillMaxSize(),
                state = state
            ) {
                items(
                    articles.size,
                ) { index ->
                    if (index == 0) {
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                    articles[index].let {
                        ArticleContentItem(
                            watchedNewsIds = watchedNewsIds,
                            article = it,
                            onArticleClick = onArticleClick,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
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
    }
}

@Composable
private fun ArticleContentItem(
    watchedNewsIds: Set<String>,
    article: Article,
    onArticleClick: (Article) -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val titleColor = if (watchedNewsIds.contains(article.id)) {
        Color(0xFFAAAAAA)
    } else {
        Grey1000
    }

    Column(
        modifier = modifier.clickableWithoutRipple(
            interactionSource = interactionSource,
        ) {
            onArticleClick(article)
        },
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Text(
            text = article.title,
            style = defaultTextStyle.copy(
                fontSize = 16.sp,
                lineHeight = 20.sp,
            ),
            color = titleColor,
            fontWeight = FontWeight.Medium,
            maxLines = 3,
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
            text = article.author + "・" + article.createdAt?.let { DateUtils.getTimeAgo(it) },
            style = defaultTextStyle.copy(
                fontSize = 14.sp,
                lineHeight = 14.sp,
            ),
            color = Color(0xFFAAAAAA)
        )
    }
}