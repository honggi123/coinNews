package com.example.coinnews.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.coinnews.R
import com.example.coinnews.ui.theme.Grey200
import com.example.coinnews.ui.theme.Grey600
import com.example.coinnews.ui.theme.Grey700
import com.example.coinnews.ui.theme.Salmon600
@Composable
fun CheckListItem(
    checked: Boolean,
    text: String,
    modifier: Modifier = Modifier,
    onClick: (Boolean) -> Unit = {},
    textColor: Color = Grey600,
) {
    val iconPainter: Painter = painterResource(
        id = if (checked) {
            R.drawable.ic_check_box
        } else {
            R.drawable.ic_empty_check_box
        }
    )
    val iconTint = if (checked) {
        Grey700
    } else {
        Grey200
    }

    Row(
        modifier = modifier.clickable(onClick = { onClick(!checked) }),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier.size(32.dp),
            painter = iconPainter,
            contentDescription = null,
            tint = iconTint
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = text,
            color = textColor,
            fontWeight = FontWeight.Normal,
        )
    }
}

@Preview
@Composable
fun CheckListItemPreview() {
    CheckListItem(
        checked = true,
        text = "text",
        onClick = {},
    )
}