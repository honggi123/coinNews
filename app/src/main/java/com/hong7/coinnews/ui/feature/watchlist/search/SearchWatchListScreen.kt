package com.hong7.coinnews.ui.feature.watchlist.search

import android.Manifest
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.paging.compose.LazyPagingItems
import com.hong7.coinnews.R
import com.hong7.coinnews.model.Coin
import com.hong7.coinnews.ui.component.SearchBar
import com.hong7.coinnews.ui.feature.watchlist.component.CoinTagItem
import com.hong7.coinnews.ui.theme.Grey100
import com.hong7.coinnews.ui.theme.Grey50
import com.hong7.coinnews.ui.theme.Grey700

@Composable
fun SearchWatchListScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: SearchWatchListViewModel = hiltViewModel()
) {
    val searchedCoins = viewModel.searchedCoins.collectAsStateWithLifecycle()
    val query = viewModel.query.collectAsStateWithLifecycle()
    val watchListCoins = viewModel.watchListCoins.collectAsStateWithLifecycle()

    SearchWatchListScreenContent(
        query = query.value,
        onBackClick = navController::popBackStack,
        onValueChange = viewModel::onQueryChanged,
        onClearQueryClick = { viewModel.onQueryChanged("") },
        searchResults = searchedCoins.value,
        watchListCoinIds = watchListCoins.value,
        onSearchResultClick = viewModel::toggleSelected,
        modifier = Modifier.fillMaxSize()
    )
}


@Composable
private fun SearchWatchListScreenContent(
    query: String,
    onValueChange: (String) -> Unit,
    onBackClick: () -> Unit,
    onClearQueryClick: () -> Unit,
    searchResults: List<Coin>,
    watchListCoinIds: Set<String>,
    onSearchResultClick: (Coin) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        SearchBar(
            query = query,
            onValueChange = onValueChange,
            onBackClick = onBackClick,
            onClearQueryClick = onClearQueryClick,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
        )

        Divider(
            modifier = Modifier,
            color = Grey100,
            thickness = 1.dp,
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.weight(1F),
            contentPadding = PaddingValues(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // todo: empty_result
            item(span = { GridItemSpan(2) }) {
                Text(
                    text = "코인 목록",
                    color = Grey700,
                    style = MaterialTheme.typography.body2.merge(
                        fontWeight = FontWeight.Bold,
                    ),
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            items(searchResults.size) { index ->
                searchResults[index].let { result ->
                    SearchItem(
                        selected = watchListCoinIds.contains(result.id),
                        name = "${result.name} (${result.symbol})",
                        onClick = {
                            onSearchResultClick(result)
                        },
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun SearchItem(
    selected: Boolean,
    name: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .run {
                if (selected) {
                    background(Grey50, RoundedCornerShape(12.dp))
                } else {
                    this
                }
            }
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            ),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CoinTagItem(
            name = name,
            selected = selected,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

