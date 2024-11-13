//package com.hong7.coinnews.ui.feature.deprecated.recentnews
//
//import android.annotation.SuppressLint
//import android.util.Log
//import androidx.compose.foundation.background
//import androidx.compose.foundation.interaction.MutableInteractionSource
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.PaddingValues
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.LazyListState
//import androidx.compose.foundation.lazy.rememberLazyListState
//import androidx.compose.material3.CircularProgressIndicator
//import androidx.compose.material3.HorizontalDivider
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.DisposableEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.hilt.navigation.compose.hiltViewModel
//import androidx.lifecycle.compose.collectAsStateWithLifecycle
//import androidx.navigation.NavHostController
//import androidx.paging.LoadState
//import androidx.paging.compose.LazyPagingItems
//import androidx.paging.compose.collectAsLazyPagingItems
//import androidx.paging.compose.itemKey
//import com.hong7.coinnews.model.News
//import com.hong7.coinnews.ui.NewsDetailNav
//import com.hong7.coinnews.ui.extensions.clickableWithoutRipple
//import com.hong7.coinnews.ui.theme.Grey1000
//import com.hong7.coinnews.ui.theme.Grey200
//import com.hong7.coinnews.ui.theme.defaultTextStyle
//import com.hong7.coinnews.utils.DateUtils
//import com.hong7.coinnews.utils.NavigationUtils
//
//@Composable
//fun RecentNewsScreen(
//    navController: NavHostController,
//    viewModel: RecentNewsViewModel = hiltViewModel()
//) {
//    val pagingItems: LazyPagingItems<News> = viewModel.pagingNews.collectAsLazyPagingItems()
//    val listState = rememberLazyListState()
//
//    RecentScreenContent(
//        pagingItems = pagingItems,
//        onNewsClick = {
//            NavigationUtils.navigate(
//                controller = navController,
//                routeName = NewsDetailNav.navigateWithArg(it)
//            )
//        },
//        modifier = Modifier.fillMaxWidth(),
//        state = listState
//    )
//}
//
//
//@Composable
//private fun LoadingContent(
//    modifier: Modifier = Modifier
//) {
//    Column(
//        modifier = modifier,
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
//    ) {
//        CircularProgressIndicator(
//            modifier = Modifier.size(38.dp)
//        )
//    }
//}
//
//@Composable
//private fun RecentScreenContent(
//    pagingItems: LazyPagingItems<News>,
//    onNewsClick: (News) -> Unit,
//    state: LazyListState,
//    modifier: Modifier = Modifier,
//    contentPadding: PaddingValues = PaddingValues(0.dp),
//) {
//    Column(
//        modifier = modifier,
//        verticalArrangement = Arrangement.spacedBy(15.dp)
//    ) {
//        HorizontalDivider(
//            thickness = 0.7.dp,
//            color = Grey200,
//        )
//        NewsList(
//            pagingItems = pagingItems,
//            onNewsClick = onNewsClick,
//            modifier = Modifier.fillMaxWidth(),
//            state = state
//        )
//    }
//}
//
//@Composable
//private fun NewsList(
//    pagingItems: LazyPagingItems<News>,
//    onNewsClick: (News) -> Unit,
//    state: LazyListState,
//    modifier: Modifier = Modifier,
//    contentPadding: PaddingValues = PaddingValues(0.dp),
//) {
//    LazyColumn(
//        contentPadding = contentPadding,
//        modifier = modifier,
//        state = state
//    ) {
//        items(
//            pagingItems.itemCount,
//        ) { index ->
//            if (index == 0) {
//                Spacer(modifier = Modifier.height(10.dp))
//            }
//            pagingItems[index]?.let {
//                NewsContentItem(
//                    news = it,
//                    onNewsClick = onNewsClick,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(horizontal = 16.dp)
//                )
//                HorizontalDivider(
//                    thickness = 0.7.dp,
//                    color = Grey200,
//                    modifier = Modifier.padding(vertical = 15.dp)
//                )
//            }
//        }
//
//
////        if (pagingItems.loadState.append is LoadState.Loading) {
////            item {
////                Box(modifier = Modifier.fillMaxWidth()) {
////                    CircularProgressIndicator(
////                        modifier = Modifier
////                            .align(Alignment.Center)
////                            .padding(16.dp)
////                    )
////                }
////            }
////        }
//    }
//    if (pagingItems.loadState.refresh is LoadState.Loading) {
//        LoadingContent(
//            modifier = Modifier.fillMaxSize()
//        )
//    }
//}
//
//@Composable
//private fun NewsContentItem(
//    news: News,
//    onNewsClick: (News) -> Unit,
//    modifier: Modifier = Modifier
//) {
//    val interactionSource = remember { MutableInteractionSource() }
//    val titleColor = Grey1000
//
//
//    Column(
//        modifier = modifier.clickableWithoutRipple(
//            interactionSource = interactionSource,
//        ) {
//            onNewsClick(news)
//        },
//        verticalArrangement = Arrangement.spacedBy(10.dp),
//    ) {
//        Text(
//            text = news.title,
//            style = defaultTextStyle.copy(
//                fontSize = 16.sp,
//                lineHeight = 20.sp,
//            ),
//            color = titleColor,
//            fontWeight = FontWeight.Bold,
//            maxLines = 3,
//        )
//        NewsMetaData(
//            news = news,
//            modifier = Modifier.fillMaxWidth()
//        )
//    }
//}
//
//@Composable
//private fun NewsMetaData(
//    news: News,
//    modifier: Modifier = Modifier
//) {
//    Row(
//        modifier = modifier,
//        horizontalArrangement = Arrangement.spacedBy(2.dp)
//    ) {
//        Text(
//            text = news.author + "・" + news.createdAt?.let { DateUtils.getTimeAgo(it) },
//            style = defaultTextStyle.copy(
//                fontSize = 14.sp,
//                lineHeight = 14.sp,
//            ),
//            color = Color(0xFFAAAAAA)
//        )
//    }
//}