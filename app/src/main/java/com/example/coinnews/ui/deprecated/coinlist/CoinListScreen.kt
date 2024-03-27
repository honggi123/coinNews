//package com.example.coinnews.ui.deprecated.coinlist
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.PaddingValues
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.LazyListState
//import androidx.compose.foundation.lazy.rememberLazyListState
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.res.stringResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.tooling.preview.PreviewParameter
//import androidx.compose.ui.tooling.preview.PreviewParameterProvider
//import androidx.compose.ui.unit.dp
//import androidx.hilt.navigation.compose.hiltViewModel
//import androidx.lifecycle.compose.collectAsStateWithLifecycle
//import androidx.paging.PagingData
//import androidx.paging.compose.LazyPagingItems
//import androidx.paging.compose.collectAsLazyPagingItems
//import com.example.coinnews.R
//import com.example.coinnews.model.Asset
//import com.example.coinnews.model.Coin
//import com.example.coinnews.model.Ordering
//import com.example.coinnews.model.Sort
//import com.example.coinnews.model.SortOption
//import com.example.coinnews.ui.components.SortableTitle
//import com.example.coinnews.ui.theme.CoinNewsAppTheme
//import com.example.coinnews.ui.utils.formatDoubleWithUnit
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.flowOf
//
//@Composable
//fun CoinListScreen(
//    onCoinClick: (Coin) -> Unit,
//    viewModel: CoinListViewModel = hiltViewModel()
//) {
//    val coins = viewModel.coins.collectAsLazyPagingItems()
//    val selectedSort by viewModel.selectedSort.collectAsStateWithLifecycle()
//
//    CoinListScreen(
//        selectedSort = selectedSort,
//        onSortClick = viewModel::onSortClick,
//        onCoinClick = onCoinClick,
//        coins = coins
//    )
//}
//
//@Composable
//private fun CoinListScreen(
//    coins: LazyPagingItems<Coin>,
//    selectedSort: Sort?,
//    onSortClick: (Sort) -> Unit,
//    onCoinClick: (Coin) -> Unit,
//    modifier: Modifier = Modifier,
//    contentPadding: PaddingValues = PaddingValues(0.dp),
//    state: LazyListState = rememberLazyListState()
//) {
//    LazyColumn(
//        contentPadding = contentPadding,
//        modifier = modifier,
//        state = state
//    ) {
//        item {
//            TitleItem(
//                selectedSort = selectedSort,
//                onSortClick = onSortClick
//            )
//            Spacer(modifier = Modifier.height(15.dp))
//        }
//        items(coins.itemCount) { index ->
//            coins[index]?.let { coin ->
//                ContentItem(
//                    coin = coin,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(50.dp)
//                        .clickable { onCoinClick(coin) }
//                )
//                Spacer(modifier = Modifier.height(10.dp))
//            }
//        }
//    }
//}
//
//@Composable
//private fun TitleItem(
//    selectedSort: Sort?,
//    onSortClick: (Sort) -> Unit,
//    modifier: Modifier = Modifier
//) {
//    val marketCapOrdering =
//        if (selectedSort?.option == SortOption.MarketCap) selectedSort.ordering else Ordering.None
//    val priceOrdering =
//        if (selectedSort?.option == SortOption.Price) selectedSort.ordering else Ordering.None
//    val priceChangeOrdering =
//        if (selectedSort?.option == SortOption.PriceChange24h) selectedSort.ordering else Ordering.None
//
//    Row(
//        modifier = modifier,
//        horizontalArrangement = Arrangement.spacedBy(10.dp),
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        Text(
//            text = stringResource(id = R.string.hash_tag),
//            style = MaterialTheme.typography.labelMedium,
//            fontWeight = FontWeight.Normal,
//            textAlign = TextAlign.Center,
//            modifier = Modifier.weight(0.5f)
//        )
//        SortableTitle(
//            title = stringResource(id = R.string.market_cap),
//            ordering = marketCapOrdering,
//            onClick = {
//                onSortClick(Sort(SortOption.MarketCap, marketCapOrdering))
//            },
//            modifier = Modifier.weight(1f),
//        )
//        SortableTitle(
//            title = stringResource(id = R.string.price),
//            ordering = priceOrdering,
//            onClick = {
//                onSortClick(Sort(SortOption.Price, priceOrdering))
//            },
//            modifier = Modifier.weight(1f),
//            horizontalArrangement = Arrangement.End
//        )
//        SortableTitle(
//            title = stringResource(id = R.string.percent_change_24h),
//            ordering = priceChangeOrdering,
//            onClick = {
//                onSortClick(Sort(SortOption.PriceChange24h, priceChangeOrdering))
//            },
//            modifier = Modifier.weight(1f),
//            horizontalArrangement = Arrangement.End
//        )
//    }
//}
//
//
//@Composable
//private fun ContentItem(
//    coin: Coin,
//    modifier: Modifier = Modifier
//) {
//    Row(
//        modifier = modifier,
//        horizontalArrangement = Arrangement.spacedBy(10.dp),
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        Text(
//            text = coin.rank.toString(),
//            style = MaterialTheme.typography.bodyLarge,
//            fontWeight = FontWeight.Bold,
//            textAlign = TextAlign.Center,
//            modifier = Modifier.weight(0.5f)
//        )
//        Column(
//            modifier = Modifier.weight(1f)
//        ) {
//            Text(
//                text = coin.symbol ?: "정보 없음",
//                style = MaterialTheme.typography.bodyMedium,
//                maxLines = 1,
//                fontWeight = FontWeight.Bold
//            )
//            Text(
//                text = formatDoubleWithUnit(coin.usdAsset?.totalMarketCap) ?: "정보 없음",
//                style = MaterialTheme.typography.bodyMedium,
//                fontWeight = FontWeight.Normal
//            )
//        }
//        Text(
//            text = formatDoubleWithUnit(coin.usdAsset?.price) ?: "정보 없음",
//            style = MaterialTheme.typography.bodyMedium,
//            fontWeight = FontWeight.Bold,
//            maxLines = 1,
//            textAlign = TextAlign.Right,
//            modifier = Modifier.weight(1f)
//        )
//        Text(
//            text = formatDoubleWithUnit(coin.usdAsset?.priceChange24h, "") ?: "정보 없음",
//            style = MaterialTheme.typography.bodyMedium,
//            fontWeight = FontWeight.Bold,
//            maxLines = 1,
//            textAlign = TextAlign.Right,
//            modifier = Modifier.weight(1f)
//        )
//    }
//}
//
//@Preview
//@Composable
//fun PreviewCoinContent(
//    @PreviewParameter(CoinContentPreviewParamProvider::class) coins: Flow<PagingData<Coin>>
//) {
//    CoinNewsAppTheme {
//        CoinListScreen(
//            coins = coins.collectAsLazyPagingItems(),
//            selectedSort = Sort(SortOption.MarketCap, Ordering.Descending),
//            onSortClick = { _ -> },
//            onCoinClick = { _ -> },
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(10.dp)
//                .background(Color.White)
//        )
//    }
//}
//
//private class CoinContentPreviewParamProvider :
//    PreviewParameterProvider<Flow<PagingData<Coin>>> {
//
//    override val values: Sequence<Flow<PagingData<Coin>>> =
//        sequenceOf(
//            flowOf(
//                PagingData.from(
//                    listOf(
//                        Coin(
//                            id = 1,
//                            rank = 1,
//                            symbol = "BTC",
//                            name = "Bitcoin",
//                            slug = "",
//                            usdAsset = Asset(
//                                price = 60000.0,
//                                priceChange24h = 13.5,
//                                totalMarketCap = (1000000..1000000000).random().toDouble()
//                            ),
//                        ),
//                        Coin(
//                            id = 2,
//                            rank = 2,
//                            symbol = "ETH",
//                            name = "Ethereum",
//                            slug = "",
//                            usdAsset = Asset(
//                                price = 2000.10,
//                                priceChange24h = 2.5,
//                                totalMarketCap = (1000000..1000000000).random().toDouble()
//                            ),
//                        )
//                    )
//                )
//            )
//        )
//}