package com.hong7.coinnews.ui.feature.watchlist.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.hong7.coinnews.R
import com.hong7.coinnews.model.CoinQuote
import com.hong7.coinnews.ui.AddWatchListNav
import com.hong7.coinnews.ui.feature.watchlist.component.CoinInfoItem
import com.hong7.coinnews.ui.theme.Grey200
import com.hong7.coinnews.ui.theme.Grey400
import com.hong7.coinnews.ui.theme.Grey50
import com.hong7.coinnews.ui.theme.Grey500
import com.hong7.coinnews.ui.theme.Grey700
import com.hong7.coinnews.ui.theme.Grey800
import com.hong7.coinnews.ui.theme.coinNewsTypography
import com.hong7.coinnews.utils.DateUtils
import com.hong7.coinnews.utils.NavigationUtils
import kotlinx.coroutines.delay
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WatchListScreen(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    viewModel: WatchListViewModel = hiltViewModel()
) {

    LaunchedEffect(Unit) {
        viewModel.messageEvent.collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "왓치리스트",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold
                        ),
                        color =
                        Grey800
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                actions = {
                    IconButton(
                        onClick = {
                            NavigationUtils.navigate(
                                navController,
                                AddWatchListNav.route
                            )
                        },
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_add_24),
                            contentDescription = "",
                            modifier = Modifier
                                .size(36.dp)
                                .padding(8.dp),
                            tint = Grey500
                        )
                    }
                },
//                scrollBehavior = scrollBehavior
            )
        }
    ) {
        when (val state = uiState.value) {
            is WatchListUiState.Success -> {
                if (state.watchList.isEmpty()) {
                    EmptyWatchList(
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    WatchListScreenContent(
                        updatedDateTime = state.updatedDateTime,
                        watchList = state.watchList,
                        onRefresh = viewModel::refresh,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(it)
                    )
                }
            }

            is WatchListUiState.Loading -> {
                LoadingContent(
                    modifier = Modifier.fillMaxSize()
                )
            }

            else -> Unit // TODO
        }
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

@Composable
fun EmptyWatchList(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "왓치 리스트의 코인을 추가해보세요!",
            style = coinNewsTypography.bodyMedium.copy(
                fontWeight = FontWeight.Normal,
                color = Grey700
            ),
        )
    }
}

@Composable
fun WatchListScreenContent(
    updatedDateTime: LocalDateTime,
    watchList: Map<String, CoinQuote>,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    var refreshing by remember { mutableStateOf(false) }
    LaunchedEffect(refreshing) {
        if (refreshing) {
            delay(3000)
            refreshing = false
        }
    }

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = refreshing),
        onRefresh = {
            onRefresh()
            refreshing = true
        },
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "업데이트 : " + DateUtils.formatLocalDateTime(updatedDateTime),
                style = coinNewsTypography.bodySmall.copy(
                    fontWeight = FontWeight.Normal,
                    color = Grey200
                ),
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.weight(1F),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(watchList.values.size) { index ->
                    val coinQuote = watchList.values.toList()[index]
                    WatchListItem(
                        coinQuote = coinQuote,
                        onClick = { }
                    )
                }
                item { 
                   Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }

}

@Composable
private fun WatchListItem(
    coinQuote: CoinQuote,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(102.dp)
            .background(Grey50, RoundedCornerShape(12.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            ),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CoinInfoItem(
            name = coinQuote.name,
            price = coinQuote.quote?.get("KRW")?.price,
            marketCap = coinQuote.quote?.get("KRW")?.marketCap,
            percentageChange24h = coinQuote.quote?.get("KRW")?.percentChange24h,
            modifier = Modifier.fillMaxWidth()
        )
    }
}


