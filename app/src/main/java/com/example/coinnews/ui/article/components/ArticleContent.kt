package com.example.coinnews.ui.article.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.coinnews.model.Article

@Composable
fun ArticleContent(
    article: Article,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    state: LazyListState = rememberLazyListState()
) {
    LazyColumn(
        contentPadding = contentPadding,
        modifier = modifier,
        state = state
    ) {
        item {
            ArticleContentItem(
                article = article,
                modifier = modifier
            )
        }
    }
}

@Composable
fun ArticleContentItem(
    article: Article,
    modifier: Modifier = Modifier
) {

}

@Composable
fun PreviewArticleContent() {

}
