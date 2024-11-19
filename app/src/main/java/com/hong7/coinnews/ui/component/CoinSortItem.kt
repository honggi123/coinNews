package com.hong7.coinnews.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.hong7.coinnews.R
import com.hong7.coinnews.ui.feature.market.model.Sort
import com.hong7.coinnews.ui.feature.market.model.SortCategory
import com.hong7.coinnews.ui.feature.market.model.SortType
import com.hong7.coinnews.ui.theme.Grey900
import com.hong7.coinnews.ui.theme.coinNewsTypography

@Composable
fun CoinSortItem(
    sortList: List<Sort>,
    selectedSort: Sort,
    onSortClick: (SortCategory, SortType) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .background(
                color = Color.White,
                shape = RoundedCornerShape(6.dp),
            )
            .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .weight(1f),
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = "종목명",
                modifier = Modifier,
                color = Grey900,
                textAlign = TextAlign.Start,
                maxLines = 1,
                style = coinNewsTypography.bodySmall.copy(
                    fontWeight = FontWeight.Medium,
                ),
            )
        }
        sortList.onEach { sort ->
            Row(
                modifier = Modifier
                    .weight(1f)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {
                            onSortClick(sort.sortCategory, selectedSort.sortType)
                        }
                    ),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = when (sort.sortCategory) {
                        SortCategory.PRICE -> "현재가"
                        SortCategory.VOLUME -> "거래량"
                        SortCategory.PERCENTAGECHANGE -> "전일대비"
                    },
                    color = Grey900,
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    style = coinNewsTypography.bodySmall.copy(
                        fontWeight = FontWeight.Medium,
                    ),
                )
                Column {
                    if (selectedSort.sortCategory == sort.sortCategory) {
                        when (selectedSort.sortType) {
                            SortType.ASCENDING -> {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_arrow_drop_up),
                                    contentDescription = "",
                                    colorFilter = ColorFilter.tint(Grey900),
                                    modifier = Modifier.size(14.dp)
                                )
                            }

                            SortType.DESCENDING -> {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_arrow_drop_down),
                                    contentDescription = "",
                                    colorFilter = ColorFilter.tint(Grey900),
                                    modifier = Modifier.size(14.dp)
                                )
                            }

                            SortType.NONE -> {

                            }
                        }
                    }
                }
            }
        }
    }
}