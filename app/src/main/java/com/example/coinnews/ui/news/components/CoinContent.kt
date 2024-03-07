package com.example.coinnews.ui.news.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.coinnews.R
import com.example.coinnews.model.Asset
import com.example.coinnews.model.Coin
import com.example.coinnews.model.CoinSortOption
import com.example.coinnews.model.Sort
import com.example.coinnews.ui.theme.CoinNewsAppTheme
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun CoinContent(
    coins: LazyPagingItems<Coin>, // todo
    sortOptions: Map<CoinSortOption, Sort>,
    onSortingClick: (CoinSortOption, Sort) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    state: LazyListState = rememberLazyListState()
) {
    LazyColumn(
        contentPadding = contentPadding,
        modifier = modifier,
        state = state
    ) {
        item {
            TitleItem(
                sortOptions = sortOptions,
                onSortingClick = onSortingClick
            )
            Spacer(modifier = Modifier.height(15.dp))
        }
        items(coins.itemCount) { index ->
            coins[index]?.let { coin ->
                ContentItem(
                    coin,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

@Composable
private fun TitleItem(
    sortOptions: Map<CoinSortOption, Sort>,
    onSortingClick: (CoinSortOption, Sort) -> Unit,
    modifier: Modifier = Modifier
) {
    val marketCapSort = sortOptions[CoinSortOption.MarketCap] ?: Sort.None
    val priceSort = sortOptions[CoinSortOption.Price] ?: Sort.None
    val priceChangeSort = sortOptions[CoinSortOption.PriceChange24h] ?: Sort.None

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(id = R.string.hash_tag),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(0.5f)
        )
        SortableCoinTitle(
            title = stringResource(id = R.string.market_cap),
            selectedSort = marketCapSort,
            onSortingClick = { onSortingClick(CoinSortOption.MarketCap, marketCapSort) },
            modifier = Modifier.weight(1f),
        )
        SortableCoinTitle(
            title = stringResource(id = R.string.price),
            selectedSort = priceSort,
            onSortingClick = { onSortingClick(CoinSortOption.Price, priceSort) },
            modifier = Modifier.weight(1f),
        )
        SortableCoinTitle(
            title = stringResource(id = R.string.percent_change_24h),
            selectedSort = priceChangeSort,
            onSortingClick = { onSortingClick(CoinSortOption.Price, priceChangeSort) },
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun SortableCoinTitle(
    title: String,
    onSortingClick: () -> Unit,
    modifier: Modifier = Modifier,
    selectedSort: Sort = Sort.None
) {
    Row(
        modifier = modifier.clickable { onSortingClick() },
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Normal,
        )
        SortableArrow(selectedSort)
    }
}

@Composable
private fun ContentItem(
    coin: Coin,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = coin.rank.toString(),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(0.5f)
        )
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = coin.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = coin.usdAsset.totalMarketCap.toString(),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Normal
            )
        }
        Text(
            text = coin.usdAsset.price.toString(),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            textAlign = TextAlign.Right,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = coin.usdAsset.priceChange24h.toString(),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            textAlign = TextAlign.Right,
            modifier = Modifier.weight(1f)
        )
    }
}

@Preview
@Composable
fun PreviewCoinContent() {
    val items = mutableListOf<Coin>()

    for (i in 1..10) {
        val coin = Coin(
            id = i,
            rank = i,
            symbol = "sym",
            name = "coin name",
            usdAsset = Asset(
                price = (1..100).random().toDouble(),
                priceChange24h = (-50..50).random().toDouble(),
                totalMarketCap = (1000000..1000000000).random().toDouble()
            )
        )
        items.add(coin)
    }

    val flow = MutableStateFlow(PagingData.from(items))

    CoinNewsAppTheme {
        CoinContent(
            coins = flow.collectAsLazyPagingItems(),
            sortOptions = mutableMapOf(CoinSortOption.Price to Sort.Descending),
            onSortingClick = { _,_ -> },
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
                .background(Color.White)
        )
    }
}