package com.hong7.coinnews.ui.feature.videolist

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.rememberAsyncImagePainter
import com.hong7.coinnews.R
import com.hong7.coinnews.model.VideoItem
import com.hong7.coinnews.ui.extensions.clickableWithoutRipple
import com.hong7.coinnews.ui.theme.Grey1000
import com.hong7.coinnews.ui.theme.Grey200
import com.hong7.coinnews.ui.theme.Grey800
import com.hong7.coinnews.ui.theme.Grey900
import com.hong7.coinnews.ui.theme.defaultTextStyle
import com.hong7.coinnews.utils.DateUtils
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoListScreen(
    snackbarHostState: SnackbarHostState,
    navController: NavHostController,
    viewModel: VideoListViewModel = hiltViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        rememberTopAppBarState()
    )
    val pagingItems = viewModel.pagingVideoItems.collectAsLazyPagingItems()

    InfluencerDetailScreenContent(
        pagingItems,
        scrollBehavior,
        onBackClick = {
            navController.popBackStack()
        },
        Modifier.fillMaxSize()
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfluencerDetailScreenContent(
    pagingItems: LazyPagingItems<VideoItem>,
    scrollBehavior: TopAppBarScrollBehavior,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold
                        ),
                        color = Grey800
                    )
                },
                navigationIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_back),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxHeight()
                            .clickable { onBackClick() },
                        tint = Grey900
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    scrolledContainerColor = Color.White
                ),
                scrollBehavior = scrollBehavior
            )
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { contentPadding ->
        videoList(
            pagingItems = pagingItems,
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        )
    }
}

@Composable
private fun TopAppBar(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(horizontal = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_back),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxHeight()
                    .clickableWithoutRipple(
                        interactionSource = interactionSource,
                    ) { onBackClick() },
                tint = Grey900
            )
            Spacer(modifier = Modifier.width(24.dp))

        }

    }
}

@Composable
fun videoList(
    pagingItems: LazyPagingItems<VideoItem>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(pagingItems.itemCount) { index ->
            pagingItems[index]?.let {
                videoItem(it)
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
fun videoItem(
    video: VideoItem,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
    ) {
        VideoWithPlayButton(video)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 20.dp,
                    vertical = 10.dp
                )
        ) {
            Text(
                text = video.snippet.title,
                style = defaultTextStyle.copy(
                    fontSize = 16.sp,
                    lineHeight = 20.sp,
                ),
                color = Grey1000,
                fontWeight = FontWeight.Bold,
                maxLines = 3,
            )
            Spacer(modifier = Modifier.padding(vertical = 3.dp))
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = video.snippet.channelTitle + "・" + video.snippet.publishedAt.let {
                        DateUtils.getTimeAgo(
                            it
                        )
                    },
                    style = defaultTextStyle.copy(
                        fontSize = 14.sp,
                        lineHeight = 14.sp,
                    ),
                    color = Color(0xFFAAAAAA)
                )
            }
        }
    }
}

@Composable
fun VideoWithPlayButton(
    video: VideoItem
) {
    if(video.snippet.resourceId.videoId == null) return
    var isPlayerReady by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(16f / 9f)
    ) {
        if (!isPlayerReady) {
            Image(
                painter = rememberAsyncImagePainter(video.snippet.thumbnails["high"]?.url),
                contentDescription = video.snippet.title,
                modifier = Modifier
                    .aspectRatio(16f / 9f)
                    .fillMaxWidth(),
                contentScale = ContentScale.FillWidth
            )
            IconButton(
                onClick = { isPlayerReady = true },
                modifier = Modifier.align(Alignment.Center)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_play_circle_outline_24),
                    contentDescription = "Play Video",
                    tint = Color.Red,
                    modifier = Modifier.size(70.dp),
                )
            }
        } else {
            AndroidView(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f),
                factory = {
                    var view = YouTubePlayerView(it)
                    view.addYouTubePlayerListener(
                        object : AbstractYouTubePlayerListener() {
                            override fun onReady(
                                youTubePlayer: YouTubePlayer
                            ) {
                                super.onReady(youTubePlayer)
                                isPlayerReady = true
                                youTubePlayer.loadVideo(video.snippet.resourceId.videoId, 0f)
                            }
                        }
                    )
                    view
                })
        }
    }
}