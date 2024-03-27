//package com.example.coinnews.ui.deprecated.coinlist.detail
//
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.Icon
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.tooling.preview.PreviewParameter
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.viewinterop.AndroidView
//import androidx.hilt.navigation.compose.hiltViewModel
//import androidx.lifecycle.compose.collectAsStateWithLifecycle
//import androidx.paging.PagingData
//import androidx.paging.compose.collectAsLazyPagingItems
//import com.example.coinnews.R
//import com.example.coinnews.model.Coin
//import com.example.coinnews.model.CoinWithInterest
//import com.example.coinnews.model.UrlType
//import com.example.coinnews.ui.theme.CoinNewsAppTheme
//import com.example.coinnews.ui.utils.formatDoubleWithUnit
//import kotlinx.coroutines.flow.Flow
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun CoinDetailScreen(
//    isInterested: Boolean,
//    coin: Coin?,
//    onBackClick: () -> Unit,
//    onToggleClick: (CoinWithInterest) -> Unit
//) {
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                coin = coin,
//                isInterested = isInterested,
//                onBackClick = onBackClick,
//                onToggleClick = onToggleClick,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = 16.dp, vertical = 16.dp)
//            )
//        }
//    ) { paddingValues ->
//        CoinDetailContent(
//            coin = coin,
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(paddingValues)
//                .padding(10.dp)
//        )
//    }
//}
//
//@Composable
//private fun TopAppBar(
//    coin: Coin?,
//    isInterested: Boolean,
//    onBackClick: () -> Unit,
//    onToggleClick: (CoinWithInterest) -> Unit,
//    modifier: Modifier = Modifier
//) {
//    val interestIconPainter = if (isInterested) {
//        painterResource(id = R.drawable.ic_star_fill)
//    } else {
//        painterResource(id = R.drawable.ic_star_border)
//    }
//
//    Row(
//        modifier = modifier
//    ) {
//        Icon(
//            painter = painterResource(id = R.drawable.ic_arrow_back),
//            contentDescription = null,
//            modifier = Modifier.clickable { onBackClick() },
//        )
//        Spacer(modifier = Modifier.weight(1f))
//        Icon(
//            painter = interestIconPainter,
//            contentDescription = null,
//            modifier = Modifier.clickable {
//                if (coin != null) {
//                    onToggleClick(CoinWithInterest(coin, isInterested))
//                }
//            }
//        )
//    }
//}
//
//@Composable
//private fun CoinDetailContent(
//    coin: Coin?,
//    modifier: Modifier = Modifier
//) {
//    if (coin == null) {
//        EmptyContent(modifier)
//    } else {
//        Column(
//            modifier = modifier,
//            verticalArrangement = Arrangement.spacedBy(10.dp)
//        ) {
//            Text(
//                text = coin.name,
//                style = MaterialTheme.typography.bodyMedium,
//                fontWeight = FontWeight.Bold,
//                maxLines = 1,
//                textAlign = TextAlign.Right,
//            )
//            Column(
//                modifier = Modifier.weight(1f),
//                verticalArrangement = Arrangement.spacedBy(20.dp)
//            ) {
//                Text(
//                    text = coin.description.toString(),
//                    style = MaterialTheme.typography.bodyMedium,
//                    fontWeight = FontWeight.Bold,
//                )
//                UrlsContent(
//                    coin = coin,
//                    modifier = Modifier.fillMaxWidth()
//                )
//            }
//        }
//    }
//}
//
//@Composable
//private fun UrlsContent(
//    coin: Coin?,
//    modifier: Modifier = Modifier
//) {
//    val webSiteUrl = coin?.urls?.get(UrlType.Website)
//    val githubUrl = coin?.urls?.get(UrlType.Gtihub)
//    val techDocUrl = coin?.urls?.get(UrlType.TechicalDoc)
//
//    Column(
//        modifier = modifier,
//        verticalArrangement = Arrangement.spacedBy(10.dp)
//    ) {
//        Text(
//            text = "webSite : ${webSiteUrl}",
//            style = MaterialTheme.typography.bodyMedium,
//            fontWeight = FontWeight.Bold,
//        )
//        Text(
//            text = "github : ${githubUrl}",
//            style = MaterialTheme.typography.bodyMedium,
//            fontWeight = FontWeight.Bold,
//        )
//        Text(
//            text = "techDoc : ${techDocUrl}",
//            style = MaterialTheme.typography.bodyMedium,
//            fontWeight = FontWeight.Bold,
//        )
//    }
//}
//
//@Composable
//private fun EmptyContent(
//    modifier: Modifier = Modifier
//) {
//    Column(
//        modifier = modifier,
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
//    ) {
//        Text(
//            text = "코인 정보가 존재하지 않습니다.",
//            style = MaterialTheme.typography.bodyMedium,
//            fontWeight = FontWeight.Bold,
//            maxLines = 1,
//            textAlign = TextAlign.Center,
//        )
//    }
//}
//
//@Preview
//@Composable
//fun PreviewCoinContent() {
//    CoinNewsAppTheme {
//        CoinDetailScreen(
//            isInterested = true,
//            coin = Coin(
//                id = 1,
//                name = "bitcoin",
//                description = "Bitcoin is a decentralized digital currency that operates without a central authority or intermediary. " +
//                        "It enables peer-to-peer transactions, allowing users to send and receive payments directly without the need for a financial institution. ",
//                slug = "btc"
//            ),
//            onBackClick = {},
//            onToggleClick = { _ -> }
//        )
//    }
//}
