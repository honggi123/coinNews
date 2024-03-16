package com.example.coinnews.ui.components

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.coinnews.R
import com.example.coinnews.model.Ordering

@Composable
fun OrderingArrow(
    ordering: Ordering,
    modifier: Modifier = Modifier,
) {
    val arrowPainter = if (ordering == Ordering.Ascending) {
        painterResource(id = R.drawable.ic_arrow_drop_up)
    } else if (ordering == Ordering.Descending) {
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
    OrderingArrow(Ordering.Ascending)
}