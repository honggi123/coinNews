package com.hong7.coinnews.ui.coinlist

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.IconButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.hong7.coinnews.model.Coin
import com.hong7.coinnews.ui.ArticleDetailNav
import com.hong7.coinnews.ui.articlelist.ArticleListViewModel
import com.hong7.coinnews.ui.component.CheckListItem
import com.hong7.coinnews.ui.theme.Grey100
import com.hong7.coinnews.ui.theme.Grey1000
import com.hong7.coinnews.ui.theme.Salmon600
import com.hong7.coinnews.utils.NavigationUtils
import com.hong7.coinnews.R
import com.hong7.coinnews.ui.theme.Blue800
import com.hong7.coinnews.ui.theme.Grey200
import com.hong7.coinnews.ui.theme.Grey700

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoinListScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: CoinListViewModel = hiltViewModel()
) {
    val coins by viewModel.coins.collectAsState()
    val selectedCoins = rememberSaveable { mutableStateOf<Set<Coin>>(mutableSetOf()) }

    LaunchedEffect(key1 = coins) {
        selectedCoins.value = coins.filter { it.isSelected }.toMutableSet()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "코인 목록",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Grey1000
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() },
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_back),
                            contentDescription = "",
                            tint = Grey700
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
//                scrollBehavior = scrollBehavior
            )
        }
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .weight(1f)
            ) {
                coins.forEach { coin ->
                    CheckListItem(
                        checked = selectedCoins.value.contains(coin),
                        onClick = {
                            if (it) {
                                val currentSet = selectedCoins.value.toMutableSet()
                                currentSet.add(coin)
                                selectedCoins.value = currentSet
                            } else {
                                val currentSet = selectedCoins.value.toMutableSet()
                                currentSet.remove(coin)
                                selectedCoins.value = currentSet
                            }
                        },
                        text = coin.name
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                }
            }
            registerCoinButton(
                actionName = "등록",
                onActionClick = {
                    viewModel.onCompleteSelect(selectedCoins.value.toList())
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun registerCoinButton(
    actionName: String,
    onActionClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        Divider(color = Grey100, thickness = 1.dp)
        Row(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            androidx.compose.material.Text(
                text = actionName,
                modifier = Modifier
                    .weight(1F)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Blue800)
                    .clickable(onClick = onActionClick)
                    .padding(vertical = 16.dp),
                color = White,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
        }
    }
}