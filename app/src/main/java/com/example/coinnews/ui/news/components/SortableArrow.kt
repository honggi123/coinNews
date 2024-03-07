package com.example.coinnews.ui.news.components

import androidx.compose.foundation.clickable
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.coinnews.model.Sort
import com.example.coinnews.R

@Composable
fun SortableArrow(
    sort: Sort,
    modifier: Modifier = Modifier
) {
    val arrowPainter = if (sort == Sort.Ascending) {
        painterResource(id = R.drawable.ic_arrow_drop_up)
    } else if (sort == Sort.Descending) {
        painterResource(id = R.drawable.ic_arrow_drop_down)
    } else {
        null
    }

    arrowPainter?.let { painter ->
        Icon(
            painter = painter,
            contentDescription = null,
            modifier = modifier
        )
    }
}

@Preview
@Composable
fun PreviewSortableArrow() {
    SortableArrow(Sort.Ascending)
}