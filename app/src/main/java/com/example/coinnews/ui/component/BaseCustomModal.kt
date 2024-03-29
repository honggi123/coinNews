package com.example.coinnews.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.coinnews.ui.theme.CoinNewsAppTheme
import com.example.coinnews.ui.theme.Grey1000
import com.example.coinnews.ui.theme.Grey200
import com.example.coinnews.ui.theme.Grey700
import com.example.coinnews.ui.theme.Salmon200
import com.example.coinnews.ui.theme.Salmon600

@Composable
fun BaseCustomModal(
    onDismissClick: () -> Unit,
    onActionClick: () -> Unit,
    modifier: Modifier = Modifier,
    dismissButtonText: String = "닫기",
    actionButtonText: String = "완료",
    dismissButtonTextColor: Color = Grey1000,
    actionButtonTextColor: Color = Color.White,
    dismissButtonBackgroundColor: Color = Grey200,
    actionButtonBackgroundColor: Color = Grey700,
    content: @Composable ColumnScope.() -> Unit,
) {
    Box(
        modifier = modifier
            .wrapContentSize()
            .background(
                color = Color.White,
                shape = RoundedCornerShape(24.dp),
            ),
        contentAlignment = Alignment.TopCenter,
    ) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .padding(horizontal = 20.dp)
                .padding(top = 10.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(28.dp)
        ) {
            ModalHandleBar(modifier = Modifier.align(Alignment.CenterHorizontally))
            this@Column.content()
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = onDismissClick,
                    text = dismissButtonText,
                    textColor = dismissButtonTextColor,
                    backgroundColor = dismissButtonBackgroundColor,
                    modifier = Modifier.weight(1F),
                )
                Button(
                    onClick = onActionClick,
                    text = actionButtonText,
                    textColor = actionButtonTextColor,
                    backgroundColor = actionButtonBackgroundColor,
                    modifier = Modifier.weight(1F),
                )
            }
        }
    }
}

@Composable
private fun ModalHandleBar(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .width(64.dp)
            .height(6.dp)
            .background(
                color = Grey200,
                shape = CircleShape,
            )
    )
}

@Composable
private fun Button(
    onClick: () -> Unit,
    text: String,
    textColor: Color,
    backgroundColor: Color,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        Text(
            text = text,
            color = textColor,
            fontWeight = FontWeight.Medium,
        )
    }
}

@Preview
@Composable
fun BaseCustomModalPreview(){
    CoinNewsAppTheme {
        BaseCustomModal(
            onDismissClick = {},
            onActionClick = {},
            modifier = Modifier.fillMaxWidth(),
        ){}
    }
}