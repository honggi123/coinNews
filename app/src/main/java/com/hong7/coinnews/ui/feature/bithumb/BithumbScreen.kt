package com.hong7.coinnews.ui.feature.bithumb

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.hong7.coinnews.model.Coin
import com.hong7.coinnews.ui.component.CoinItem
import com.hong7.coinnews.ui.component.CoinSortItem
import com.hong7.coinnews.ui.feature.market.model.Sort
import com.hong7.coinnews.ui.feature.market.model.SortCategory
import com.hong7.coinnews.ui.feature.market.model.SortType
import kotlinx.coroutines.delay
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BithumbScreen(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    viewModel: BithumbViewModel = hiltViewModel()
) {

    LaunchedEffect(Unit) {
        viewModel.messageEvent.collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold {
        when (val state = uiState.value) {
            is MarketUiState.Success -> {
                MarketScreenContent(
                    sortList = state.sortList,
                    selectedSort = state.selectedSort,
                    updatedDateTime = state.updatedDateTime,
                    onSortClick = viewModel::onSortClick,
                    coinList = state.coinList,
                    onRefresh = viewModel::refresh,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                )
            }

            is MarketUiState.Loading -> {
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
fun MarketScreenContent(
    updatedDateTime: LocalDateTime,
    sortList: List<Sort>,
    selectedSort: Sort,
    onSortClick: (SortCategory, SortType) -> Unit,
    coinList: List<Coin>,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
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
                .padding(horizontal = 6.dp, vertical = 6.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CoinSortItem(
                sortList = sortList,
                selectedSort = selectedSort,
                onSortClick = onSortClick,
                modifier = Modifier.fillMaxWidth()
            )
            HorizontalDivider()
            LazyColumn(
                modifier = Modifier.weight(1F),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                items(coinList.size) { index ->
                    MarketListItem(
                        coin = coinList[index],
                        onClick = {
//                            val intent = Intent(
//                                Intent.ACTION_VIEW,
//                                Uri.parse("https://coinmarketcap.com/ko/currencies/${coinQuote.slug}")
//                            )
//                            context.startActivity(intent)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
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
private fun MarketListItem(
    coin: Coin,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            ),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CoinItem(
            name = coin.koreanName,
            ticker = coin.marketId,
            price = coin.tradePrice,
            volume24h = coin.accTradePrice24h,
            percentageChange24h = coin.changeRate,
            modifier = Modifier.fillMaxWidth()
        )
    }
}


