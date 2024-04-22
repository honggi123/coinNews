package com.hong7.coinnews.ui.coinlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hong7.coinnews.ui.articlelist.ArticleListViewModel
import com.hong7.coinnews.ui.component.CheckListItem

@Composable
fun CoinListScreen(
    modifier: Modifier = Modifier,
    viewModel: CoinListViewModel = hiltViewModel()

) {
    val coins by viewModel.coins.collectAsState()

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        coins?.forEachIndexed { index, coinFilter ->
            // todo
        }
    }
}