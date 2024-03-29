package com.example.coinnews.ui.component

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.coinnews.ui.theme.CoinNewsAppTheme
import com.example.coinnews.ui.theme.Grey1000
import com.example.coinnews.ui.theme.Grey200
import com.example.coinnews.ui.theme.Grey700

@Composable
fun ClickableChip(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 5.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Grey700,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
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
            ClickableChip(
                text = "+",
                onClick = {},
                modifier = Modifier
                    .width(100.dp)
                    .height(50.dp)
            )
        }
    }
}