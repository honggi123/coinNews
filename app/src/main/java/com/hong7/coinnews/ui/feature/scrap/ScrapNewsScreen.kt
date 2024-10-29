package com.hong7.coinnews.ui.feature.scrap

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.IconButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.hong7.coinnews.R
import com.hong7.coinnews.model.News
import com.hong7.coinnews.model.Coin
import com.hong7.coinnews.ui.NewsDetailNav
import com.hong7.coinnews.ui.SettingNav
import com.hong7.coinnews.ui.theme.Blue800
import com.hong7.coinnews.ui.theme.Grey200
import com.hong7.coinnews.ui.theme.Grey300
import com.hong7.coinnews.ui.theme.Grey500
import com.hong7.coinnews.ui.theme.Grey700
import com.hong7.coinnews.ui.theme.defaultTextStyle
import com.hong7.coinnews.utils.DateUtils
import com.hong7.coinnews.utils.NavigationUtils

@Composable
fun ScrapNewsScreen(
    navController: NavHostController,
    viewModel: ScrapNewsViewModel = hiltViewModel()
) {
    val newsList by viewModel.newsList.collectAsState()

    ScrapNewsScreen(
        newsList = newsList,
        onNewsClick = {
            NavigationUtils.navigate(
                navController,
                NewsDetailNav.navigateWithArg(it)
            )
        },
        onSettingClick = {
            NavigationUtils.navigate(
                navController,
                SettingNav.route
            )
        },
        modifier = Modifier.fillMaxSize()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScrapNewsScreen(
    newsList: List<News>,
    onSettingClick: () -> Unit,
    onNewsClick: (News) -> Unit,
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState()
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Scrap News",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold
                        ),
                        color = Blue800
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                actions = {
                    IconButton(
                        onClick = { onSettingClick() },
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_info_24),
                            contentDescription = "",
                            tint = Grey500
                        )
                    }
                },
//                scrollBehavior = scrollBehavior
            )
        },
        modifier = modifier
    ) { contentPadding ->
        if (newsList.isEmpty()) {
            EmptyNewsContent(
                text = "스크랩 뉴스가 존재하지 않습니다.",
                modifier = modifier.padding(contentPadding)
            )
        } else {
            LazyColumn(
                contentPadding = contentPadding,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp, start = 12.dp, end = 12.dp),
                state = state
            ) {
                items(
                    items = newsList,
                    key = { coin -> coin.id }
                ) {
                    NewsContentItem(
                        news = it,
                        onNewsClick = onNewsClick,
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
private fun NewsContentItem(
    news: News,
    onNewsClick: (News) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.clickable { onNewsClick(news) },
        verticalArrangement = Arrangement.spacedBy(6.dp),
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            text = news.title,
            style = defaultTextStyle.copy(
                fontSize = 16.sp,
                lineHeight = 20.sp,
            ),
            fontWeight = FontWeight.Bold,
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
            text = news.author ?: "-",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Normal,
            color = Color(0xFFAAAAAA)

        )
        Text(
            text = "・",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Normal,
            color = Color(0xFFAAAAAA)
        )
        Text(
            text = news.createdAt?.let { DateUtils.getTimeAgo(it) } ?: "",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Normal,
            color = Color(0xFFAAAAAA)
        )
    }
}





