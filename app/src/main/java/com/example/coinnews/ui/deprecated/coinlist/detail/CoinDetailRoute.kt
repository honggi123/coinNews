//package com.example.coinnews.ui.deprecated.coinlist.detail
//
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.hilt.navigation.compose.hiltViewModel
//import androidx.lifecycle.compose.collectAsStateWithLifecycle
//
//@Composable
//fun CoinDetailRoute(
//    onBackClick: () -> Unit,
//    viewModel: CoinDetailViewModel = hiltViewModel()
//) {
//    val isInterested by viewModel.isInterested.collectAsStateWithLifecycle(false)
//    val coin by viewModel.coinInfo.collectAsStateWithLifecycle(null)
//
//    // todo add loading state
//
//    CoinDetailScreen(
//        isInterested = isInterested,
//        coin = coin,
//        onBackClick = onBackClick,
//        onToggleClick = viewModel::toggleInterest
//    )
//}
