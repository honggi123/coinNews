package com.example.coinnews.ui.scrap

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.coinnews.R
import com.example.coinnews.model.Coin
import com.example.coinnews.ui.components.SortableTitle

@Composable
fun ScrapNewsScreen(
    viewModel: ScrapNewsViewModel = hiltViewModel()
) {
//    val coins by viewModel.coins.collectAsStateWithLifecycle(emptyList())
//
//    InterestNewsScreen(
//        coins = coins,
//        onDeleteClick = viewModel::deleteInterest,
//        modifier = Modifier.fillMaxSize()
//    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScrapNewsScreen(
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
            modifier = modifier.padding(horizontal = 10.dp),
            state = state
        ) {
            items(
                items = coins,
                key = { coin -> coin.id }
            ) { coin ->
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
            text = "${coin.name} (${coin.symbol})",
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )
        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_delete),
            contentDescription = null,
            modifier = Modifier
                .clickable { onDeleteClick(coin) }
                .weight(0.5f)
        )
    }
}




