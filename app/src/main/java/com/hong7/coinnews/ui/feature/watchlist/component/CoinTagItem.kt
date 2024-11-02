package com.hong7.coinnews.ui.feature.watchlist.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.hong7.coinnews.ui.theme.Grey200
import com.hong7.coinnews.ui.theme.Grey700

@Composable
fun CoinTagItem(
    name: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
) {
    val backgroundColor = if(selected){
        Grey700
    } else {
        Grey200
    }

    val textColor = if(selected){
        Color.White
    } else {
        Grey700
    }

    Box(
        modifier = modifier
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(6.dp),
            )
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.TopStart,
    ) {
        Text(
            text = name,
            modifier = Modifier,
            color = textColor,
            textAlign = TextAlign.Start,
            maxLines = 1,
            style = MaterialTheme.typography.body2.copy(
                fontWeight = FontWeight.Medium,
            ),
        )
    }
}