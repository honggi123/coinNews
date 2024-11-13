//package com.hong7.coinnews.ui.feature.deprecated
//
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.PaddingValues
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.lazy.LazyRow
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.Scaffold
//import androidx.compose.material.Text
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.SnackbarHostState
//import androidx.compose.material3.TopAppBar
//import androidx.compose.material3.TopAppBarDefaults
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.collectAsState
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.text.style.TextOverflow
//import androidx.compose.ui.unit.dp
//import androidx.hilt.navigation.compose.hiltViewModel
//import androidx.navigation.NavHostController
//import coil.compose.rememberAsyncImagePainter
//import com.hong7.coinnews.model.News
//import com.hong7.coinnews.ui.NewsDetailNav
//import com.hong7.coinnews.ui.NewsNav
//import com.hong7.coinnews.ui.VideoListNav
//import com.hong7.coinnews.ui.theme.Grey400
//import com.hong7.coinnews.ui.theme.Grey50
//import com.hong7.coinnews.ui.theme.Grey600
//import com.hong7.coinnews.ui.theme.Grey800
//import com.hong7.coinnews.utils.NavigationUtils
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun HomeScreen(
//    snackbarHostState: SnackbarHostState,
//    navController: NavHostController,
//    viewModel: InfoViewModel = hiltViewModel()
//) {
//    val news = viewModel.news.collectAsState()
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = {
//                    Text(
//                        text = "홈",
//                        style = MaterialTheme.typography.titleLarge.copy(
//                            fontWeight = FontWeight.ExtraBold
//                        ),
//                        color = Grey800
//                    )
//                },
//                colors = TopAppBarDefaults.topAppBarColors(
//                    containerColor = Color.White,
//                    scrolledContainerColor = Color.White
//                ),
//            )
//        }
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(it),
//        ) {
//            HomeScreenContent(
//                influencerList = mockInfluencerList,
//                recentNewsList = news.value,
//                onSeeAllClick = {},
//                navController = navController
//            )
//        }
//    }
//}
//
//@Composable
//internal fun HomeScreenContent(
//    influencerList: List<Influencer>,
//    recentNewsList: List<News>?,
//    onSeeAllClick: () -> Unit,
//    modifier: Modifier = Modifier,
//    navController: NavHostController
//) {
//    val context = LocalContext.current
//
//    Column(modifier = modifier) {
//        SectionHeader(
//            title = "인기 있는 인플루언서",
//            trailingText = "",
//            onTrailingClick = onSeeAllClick,
//        )
//        LazyRow(
//            horizontalArrangement = Arrangement.spacedBy(32.dp),
//            contentPadding = PaddingValues(start = 32.dp, top = 28.dp, bottom = 36.dp, end = 32.dp),
//        ) {
//            items(influencerList.size) { index ->
//                InfluencerItem(
//                    imageUrl = influencerList[index].profileUrl,
//                    name = influencerList[index].name,
//                    onClick = {
//                        NavigationUtils.navigate(
//                            navController,
//                            VideoListNav.navigateWithArg(influencerList[index].id),
//                        )
//                    },
//                    modifier = Modifier,
//                )
//            }
//        }
//        // TODO recentNewsList null hanle
//        if (recentNewsList != null) {
//            SectionHeader(
//                title = "최근 암호화폐 뉴스",
//                trailingText = "전체보기",
//                onTrailingClick = {
//                    NavigationUtils.navigate(navController, NewsNav.route)
//                },
//            )
//            LazyRow(
//                horizontalArrangement = Arrangement.spacedBy(12.dp),
//                contentPadding = PaddingValues(
//                    start = 32.dp,
//                    top = 28.dp,
//                    bottom = 36.dp,
//                    end = 32.dp
//                ),
//            ) {
//                items(recentNewsList) { news ->
//                    with(news) {
//                        RecentNewsItem(
//                            title = title,
//                            description = description,
//                            author = author,
//                            modifier = Modifier
//                                .width(256.dp)
//                                .height(126.dp)
//                                .clickable {
//                                    NavigationUtils.navigate(
//                                        navController,
//                                        NewsDetailNav.navigateWithArg(news)
//                                    )
//                                }
//                            // TODO
//                        )
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Composable
//internal fun RecentNewsItem(
//    title: String,
//    description: String,
//    author: String?,
//    modifier: Modifier = Modifier,
//) {
//    Column(
//        modifier = modifier
//            .background(color = Grey50, shape = RoundedCornerShape(12.dp))
//            .padding(horizontal = 20.dp, vertical = 16.dp),
//        verticalArrangement = Arrangement.spacedBy(24.dp)
//    ) {
//        Text(
//            text = title,
//            color = Grey800,
//            overflow = TextOverflow.Ellipsis,
//            maxLines = 2,
//            style = MaterialTheme.typography.bodyMedium.merge(
//                fontWeight = FontWeight.Bold,
//            ),
//        )
//        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
//            author?.let {
//                Text(
//                    text = author,
//                    color = Grey400,
//                    style = MaterialTheme.typography.labelMedium.copy(
//                        fontWeight = FontWeight.Medium,
//                    )
//                )
//            }
//        }
//    }
//}
//
//@Composable
//private fun InfluencerItem(
//    imageUrl: String,
//    name: String,
//    onClick: () -> Unit,
//    modifier: Modifier = Modifier,
//) {
//    Box(
//        modifier = modifier,
//        contentAlignment = Alignment.Center
//    ) {
//        Column(
//            modifier = Modifier,
//            verticalArrangement = Arrangement.spacedBy(8.dp),
//            horizontalAlignment = Alignment.CenterHorizontally,
//        ) {
//            Image(
//                painter = rememberAsyncImagePainter(
//                    model = imageUrl,
//                    contentScale = ContentScale.Crop,
//                ),
//                contentDescription = "CareProviderProfile_Image",
//                modifier = Modifier
//                    .size(108.dp)
//                    .clip(CircleShape)
//                    .background(Grey50, CircleShape)
//                    .clickable {
//                        onClick()
//                    },
//                contentScale = ContentScale.Crop,
//            )
//            Text(
//                text = name,
//                modifier = Modifier,
//                color = Grey600,
//                textAlign = TextAlign.Center,
//                overflow = TextOverflow.Ellipsis,
//                style = MaterialTheme.typography.bodyLarge.copy(
//                    fontWeight = FontWeight.Medium,
//                )
//            )
//        }
//    }
//}
//
//@Composable
//private fun SectionHeader(
//    title: String,
//    modifier: Modifier = Modifier,
//    trailingText: String = "",
//    onTrailingClick: (() -> Unit)? = null,
//) {
//    Row(
//        modifier = modifier,
//        horizontalArrangement = Arrangement.spacedBy(4.dp),
//        verticalAlignment = Alignment.Bottom
//    ) {
//        Text(
//            text = title,
//            modifier = Modifier
//                .weight(1F)
//                .padding(
//                    start = 24.dp,
//                    top = 26.dp,
//                    bottom = 12.dp,
//                ),
//            color = Grey800,
//            style = MaterialTheme.typography.titleMedium.copy(
//                fontWeight = FontWeight.Bold,
//            )
//        )
//        Text(
//            text = trailingText,
//            modifier = Modifier
//                .padding(end = 24.dp, bottom = 12.dp)
//                .run {
//                    if (onTrailingClick != null) {
//                        clickable(onClick = onTrailingClick)
//                    } else {
//                        this
//                    }
//                },
//            color = Grey400,
//            style = MaterialTheme.typography.bodyLarge
//        )
//    }
//}
//
//val mockInfluencerList = listOf(
//    Influencer(
//        "UUa0nvMcv3bQzxFTjSyp_Vvg",
//        "매억남",
//        "https://yt3.ggpht.com/ytc/AIdro_lxW1wtZfz1yYRe0RevVhGyVeAjcSDSZco4x3KwqYGJjBg=s800-c-k-c0x00ffffff-no-rj"
//    ),
//    Influencer(
//        "UUWZ6ZwIlzPydIGEi1XW4nwQ",
//        "찰리브라웅",
//        "https://yt3.ggpht.com/h31U1--WROqidKks0i_2E6ZDz7rS49msCrtrqT8S7wmyDKouO5fFZOf2w7XkO_U1HGtEsEQxNQ=s800-c-k-c0x00ffffff-no-rj"
//    ),
//    Influencer(
//        "UURAAOkMnOxEPfy7wA98iVBg",
//        "더픽",
//        "https://yt3.ggpht.com/IoX6rmD1N7DqwxFhikplA3BxH8_5QgxzwGFwlRjk4XRZAUxooVUJX6c6c4Zn42LYcwrsP1mBitk=s800-c-k-c0x00ffffff-no-rj"
//    ),
//    Influencer(
//        "UUuj_rpY3vgPHAwBrIGOLT5A",
//        "블록미디어",
//        "https://yt3.ggpht.com/ijTAIugFCjHhTuq7MPAzMHGiIZ-db9vszdr73Xq-W9BkfwyCDfaQgyqDTtuKDEMNjGCl8xzHBg=s800-c-k-c0x00ffffff-no-rj"
//    ),
//    Influencer(
//        "UUvhsQm_E8wx2t5haDnCejMg",
//        "코인이슈 경제채널",
//        "https://yt3.ggpht.com/TODiJUH5GuD6xQgb3TBlYsXmvA92XlX4R1nVl-Ts0Vmel5lV10okXHXryJUfTvfuoxCLrUYd66w=s800-c-k-c0x00ffffff-no-rj"
//    )
//)
//
//data class Influencer(
//    val id: String,
//    val name: String,
//    val profileUrl: String
//)
