package com.example.coinnews.ui.coinlist.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun CoinDetailRoute(
    onBackClick: () -> Unit,
    viewModel: CoinDetailViewModel = hiltViewModel()
) {
    val coinInfo by viewModel.coinInfo.collectAsStateWithLifecycle(null)

    // todo add loading state

    CoinDetailScreen(
        coinInfo = coinInfo,
        onBackClick = onBackClick,
        onToggleClick = viewModel::toggleInterest
    )
}
