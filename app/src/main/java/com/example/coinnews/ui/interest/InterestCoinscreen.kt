package com.example.coinnews.ui.interest

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.coinnews.R
import com.example.coinnews.model.Coin
import com.example.coinnews.ui.utils.formatDoubleWithUnit

@Composable
fun InterestCoinScreen(
    viewModel: InterestCoinViewModel = hiltViewModel()
){
    val coins by viewModel.coins.collectAsStateWithLifecycle(emptyList())

    InterestCoinScreen(
        coins = coins,
        onDeleteClick = viewModel::deleteInterest,
        modifier = Modifier.fillMaxSize()
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InterestCoinScreen(
    coins: List<Coin>,
    onDeleteClick: (coin: Coin) -> Unit,
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState()
) {
    Scaffold(
        modifier = modifier
    ) { contentPadding ->
        LazyColumn(
            contentPadding = contentPadding,
            modifier = modifier,
            state = state
        ) {
            items(coins.size) { index ->
                coins[index].let { coin ->
                    CoinItem(
                        onDeleteClick = onDeleteClick,
                        coin = coin,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}

@Composable
private fun CoinItem(
    coin: Coin,
    onDeleteClick: (coin: Coin) -> Unit,
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
                text = coin.symbol ?: "정보 없음",
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = formatDoubleWithUnit(coin.usdAsset?.totalMarketCap) ?: "정보 없음",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Normal
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_vertical_dots),
                contentDescription = null,
            )
        }
    }
}


