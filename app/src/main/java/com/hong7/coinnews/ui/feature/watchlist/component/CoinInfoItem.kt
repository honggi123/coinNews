package com.hong7.coinnews.ui.feature.watchlist.component

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
import com.hong7.coinnews.ui.theme.Green600
import com.hong7.coinnews.ui.theme.Grey200
import com.hong7.coinnews.ui.theme.Grey800
import com.hong7.coinnews.ui.theme.coinNewsTypography
import com.hong7.coinnews.utils.PriceUtils
import java.math.BigDecimal

@Composable
fun CoinInfoItem(
    name: String,
    price: Double?,
    marketCap: Double?,
    percentageChange24h: Double?,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .background(
                color = Grey800,
                shape = RoundedCornerShape(6.dp),
            )
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.SpaceBetween
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
                color = Color.White,
                textAlign = TextAlign.Start,
                maxLines = 1,
                style = coinNewsTypography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium,
                ),
            )
        }
        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = price?.let { PriceUtils.getPriceCommaString(it) + "원" } ?: "",
                modifier = Modifier,
                color = Color.White,
                textAlign = TextAlign.Start,
                maxLines = 1,
                style = coinNewsTypography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
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
//            Text(
//                text = marketCap.toString(),
//                color = Grey200,
//                textAlign = TextAlign.Start,
//                maxLines = 1,
//                style = coinNewsTypography.labelMedium.copy(
//                    fontWeight = FontWeight.Normal,
//                ),
//            )
            percentageChange24h?.let {
                Text(
                    text = PriceUtils.cutToOneDecimal(percentageChange24h).toString() + "%",
                    color = if (percentageChange24h > 0) Color.Green else Color.Red,
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    style = coinNewsTypography.bodyMedium.copy(
                        fontWeight = FontWeight.Normal,
                    ),
                )
            }
        }
    }
}

@Preview
@Composable
fun previewCoinInfoItem() {
    CoinInfoItem(
        name = "Bitcoin",
        price = 0.0,
        marketCap = 0.0,
        percentageChange24h = 4.49,
        modifier = Modifier.height(120.dp)
    )
}