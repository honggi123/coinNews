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

    SearchWatchListScreenContent(
        query = query.value,
        onBackClick = { navController.popBackStack() },
        onValueChange = viewModel::onQueryChanged,
        onClearQueryClick = { viewModel.onQueryChanged("") },
        searchResults = searchedCoins.value,
        selectedSearchResults = emptySet(),
        onSearchResultClick = {},
        onSearchClick = {},
        modifier = Modifier.fillMaxSize()
    )
}


@Composable
private fun SearchWatchListScreenContent(
    query: String,
    onValueChange: (String) -> Unit,
    onBackClick: () -> Unit,
    onSearchClick: () -> Unit,
    onClearQueryClick: () -> Unit,
    searchResults: List<Coin>,
    selectedSearchResults: Set<Coin>,
    onSearchResultClick: (Coin) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        SearchBar(
            query = query,
            onValueChange = onValueChange,
            onBackClick = onBackClick,
            onSearchClick = onSearchClick,
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
//
//        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
//            Spacer(modifier = Modifier.height(16.dp))
//
//
//            Spacer(modifier = Modifier.height(10.dp))
//
//            Text(
//                text = "headerText",
//                modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp),
//                color = Grey700,
//                style = MaterialTheme.typography.body2.merge(
//                    fontWeight = FontWeight.Bold,
//                )
//            )
//
//            Divider(
//                modifier = Modifier,
//                color = Grey100,
//                thickness = 1.dp,
//            )
//
//            Spacer(modifier = Modifier.height(16.dp))
//        }

        LazyColumn(
            modifier = Modifier.weight(1F),
            contentPadding = PaddingValues(start = 24.dp, top = 6.dp, end = 24.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            // todo: empty_result
            items(searchResults.size) { index ->
                searchResults[index].let { result ->
                    SearchItem(
                        selected = selectedSearchResults.contains(result),
                        name = "${result.name} (${result.symbol})" ,
                        onClick = {
                            onSearchResultClick(result)
                        }
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
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .run {
                if (selected) {
                    background(Grey50, RoundedCornerShape(12.dp))
                } else {
                    this
                }
            }
            .padding(horizontal = 12.dp, vertical = 16.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            ),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(
                id = if (selected) {
                    R.drawable.ic_check_box
                } else {
                    R.drawable.ic_empty_check_box
                }
            ),
            contentDescription = "NeighborhoodSearchResult_Radio",
            modifier = Modifier.size(32.dp),
            tint = Color.Unspecified,
        )

        Text(
            text = name,
            modifier = Modifier.weight(1F),
            color = Grey700,
            maxLines = 1,
            style = MaterialTheme.typography.body2.merge(
                fontWeight = FontWeight.Normal,
            )
        )
    }
}

