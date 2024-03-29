package com.example.coinnews.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.example.coinnews.model.Ordering

@Composable
fun SortableTitle(
    title: String,
    modifier: Modifier = Modifier,
    ordering: Ordering = Ordering.None,
    onClick: () -> Unit = {},
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
) {
    Row(
        modifier = modifier.clickable { onClick() },
        horizontalArrangement = horizontalArrangement
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Normal,
        )
        OrderingArrow(ordering)
    }
}