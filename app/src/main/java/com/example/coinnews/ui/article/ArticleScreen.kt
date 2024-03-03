package com.example.coinnews.ui.article

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.coinenews.R
import com.example.coinnews.model.Article
import com.example.coinnews.model.ArticleMetaData
import com.example.coinnews.ui.article.components.ArticleContent
import com.example.coinnews.ui.theme.CoinNewsAppTheme

@Composable
fun ArticleRoute(
    modifier: Modifier = Modifier,
    viewModel: ArticleViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ArticleScreen(
        uiState = uiState,
        articles = emptyList(),
        onArticlesRefresh = {},
        onArticleClick = { createArticleIntent(it) },
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ArticleScreen(
    uiState: ArticleUiState,
    articles: List<Article>,
    onArticlesRefresh: () -> Unit,
    onArticleClick: (Article) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = stringResource(id = R.string.news),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            )
        },
        modifier = modifier
    ) { contentPadding ->
        ArticleContent(
            articles = articles,
            onArticleClick = onArticleClick,
            contentPadding = contentPadding,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Composable
private fun TopAppBar(
    title: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
    }
}

private fun createArticleIntent(article: Article) {}

@Preview
@Composable
private fun PreviewArticleScreen() {
    val items = listOf(
        Article(
            id = "1",
            title = "블롬버그 \"버블 조짐 있다\"vs 월스트리트저널 \"과거 만큼은 아니다\"",
            imageUrl = "imageUrl",
            url = "url",
            metaData = ArticleMetaData(
                author = "블록미디어",
                postDate = "2시간 전"
            )
        )
    )

    CoinNewsAppTheme {
        ArticleScreen(
            uiState = ArticleUiState(
                loading = false
            ),
            articles = items,
            onArticlesRefresh = {},
            onArticleClick = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}
