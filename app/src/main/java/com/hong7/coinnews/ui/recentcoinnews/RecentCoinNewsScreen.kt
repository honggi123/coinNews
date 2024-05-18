package com.hong7.coinnews.ui.recentcoinnews

import android.annotation.SuppressLint
import com.hong7.coinnews.ui.coinlist.CoinListViewModel

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.hong7.coinnews.model.Coin
import com.hong7.coinnews.ui.component.CheckListItem
import com.hong7.coinnews.ui.theme.Grey100
import com.hong7.coinnews.ui.theme.Grey1000
import com.hong7.coinnews.R
import com.hong7.coinnews.model.Article
import com.hong7.coinnews.model.Filter
import com.hong7.coinnews.model.NetworkState
import com.hong7.coinnews.ui.ArticleDetailNav
import com.hong7.coinnews.ui.CoinListNav
import com.hong7.coinnews.ui.component.ClickableChip
import com.hong7.coinnews.ui.component.SelectableChip
import com.hong7.coinnews.ui.extensions.clickableWithoutRipple
import com.hong7.coinnews.ui.mycoinnews.MyCoinNewsViewModel
import com.hong7.coinnews.ui.theme.Blue600
import com.hong7.coinnews.ui.theme.Blue800
import com.hong7.coinnews.ui.theme.Grey200
import com.hong7.coinnews.ui.theme.Grey700
import com.hong7.coinnews.ui.theme.defaultTextStyle
import com.hong7.coinnews.utils.DateUtils
import com.hong7.coinnews.utils.NavigationUtils
import kotlinx.coroutines.launch

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun RecentCoinNewsScreen(
    networkState: NetworkState,
    navController: NavHostController,
    viewModel: RecentCoinNewsViewModel = hiltViewModel()
) {
    val state = rememberLazyListState()

    val articles by viewModel.articles.collectAsStateWithLifecycle()
    val loading by viewModel.loading.collectAsStateWithLifecycle()
    val watchedNews by viewModel.watchedNewsIds.collectAsStateWithLifecycle()

    ArticleListScreenContent(
        watchedNewsIds = watchedNews,
        isLoading = loading,
        articles = articles,
        onArticleClick = {
            NavigationUtils.saveArticle(it)
            NavigationUtils.navigate(
                navController,
                ArticleDetailNav.navigateWithArg(it)
            )
            viewModel.onNewsClick(it.id)
        },
        modifier = Modifier.fillMaxWidth(),
        state = state
    )
}

@Composable
private fun ArticleListScreenContent(
    watchedNewsIds: Set<String>,
    isLoading: Boolean,
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
            if (isLoading) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(38.dp)
                    )
                }
            }
            LazyColumn(
                contentPadding = contentPadding,
                modifier = Modifier.fillMaxSize(),
                state = state
            ) {
                items(
                    articles.size,
//                        key = { articles[it].id } todo
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