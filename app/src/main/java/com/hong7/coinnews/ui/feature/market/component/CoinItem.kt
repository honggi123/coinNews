package com.hong7.coinnews.ui.feature.market.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hong7.coinnews.ui.theme.Grey50
import com.hong7.coinnews.ui.theme.Grey700
import com.hong7.coinnews.ui.theme.Grey800
import com.hong7.coinnews.ui.theme.coinNewsTypography
import com.hong7.coinnews.utils.PriceUtils

@Composable
fun CoinItem(
    name: String,
    price: Double?,
    volume24h: Double?,
    percentageChange24h: Double?,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .background(
                color = Grey50,
                shape = RoundedCornerShape(6.dp),
            )
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = name,
                modifier = Modifier,
                color = Grey700,
                textAlign = TextAlign.Start,
                maxLines = 1,
                style = coinNewsTypography.bodySmall.copy(
                    fontWeight = FontWeight.Medium,
                ),
            )
        }
        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = price.toString(),    //todo
                modifier = Modifier,
                color = Grey700,
                textAlign = TextAlign.Start,
                maxLines = 1,
                style = coinNewsTypography.bodySmall.copy(
                    fontWeight = FontWeight.Medium,
                ),
            )
        }
        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.Bottom
        ) {
            percentageChange24h?.let {
                Text(
                    text = "${PriceUtils.roundAfterMultiplyingBy100(percentageChange24h)}%",
                    color = if (percentageChange24h > 0) Color.Green else Color.Red,
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    style = coinNewsTypography.bodySmall.copy(
                        fontWeight = FontWeight.Medium,
                    ),
                )
            }
        }
        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.Bottom
        ) {
            volume24h?.let {
                Text(
                    text = PriceUtils.formatToKoreanWon(volume24h.toLong()),
                    color = Grey700,
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    style = coinNewsTypography.bodySmall.copy(
                        fontWeight = FontWeight.Medium,
                    ),
                )
            }
        }
    }
}

@Preview
@Composable
fun previewCoinInfoItem() {
    CoinItem(
        name = "Bitcoin",
        price = 0.0,
        volume24h = 0.0,
        percentageChange24h = 4.49,
        modifier = Modifier.height(120.dp)
    )
}