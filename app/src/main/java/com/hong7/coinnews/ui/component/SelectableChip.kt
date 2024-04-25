package com.hong7.coinnews.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hong7.coinnews.ui.theme.Blue800
import com.hong7.coinnews.ui.theme.CoinNewsAppTheme
import com.hong7.coinnews.ui.theme.Grey
import com.hong7.coinnews.ui.theme.Grey1000
import com.hong7.coinnews.ui.theme.Grey200

@Composable
fun SelectableChip(
    selected: Boolean,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colorBackground = if (selected) {
        Blue800
    } else {
        Grey200
    }

    val colorText = if (selected) {
        White
    } else {
        Grey
    }

    val fontWeight = if (selected) {
        FontWeight.Bold
    } else {
        FontWeight.Normal
    }

    Box(
        modifier = modifier
            .background(colorBackground, RoundedCornerShape(6.dp))
            .clip(RoundedCornerShape(6.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = colorText,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = fontWeight
        )
    }
}

@Preview
@Composable
private fun SelectableChipPreview() {
    CoinNewsAppTheme {
        Box(
            modifier = Modifier.fillMaxSize()
                .background(Color.White)
        ) {
            SelectableChip(
                selected = false,
                text = "비트코인",
                onClick = {},
                modifier = Modifier
                    .width(100.dp)
                    .height(50.dp)
            )
        }
    }
}