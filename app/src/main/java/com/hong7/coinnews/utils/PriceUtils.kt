package com.hong7.coinnews.utils

import android.util.Log
import java.text.DecimalFormat

object PriceUtils {

    fun roundAfterMultiplyingBy100(value: Double): Double {
        return (value * 10_000).let { Math.round(it) / 100.0 }
    }

    fun formatPrice(value: Double): String {
        val decimalFormat = DecimalFormat("#.################")
        val priceStr = decimalFormat.format(value)
        val parts = priceStr.split(".")
        return if (parts[0].toInt() > 0) {
            // 1의 자리도 있는 경우
            var formattedValue = parts[0].replace(Regex("(\\d)(?=(\\d{3})+(?!\\d))"), "$1,")
            formattedValue
        } else {
            // 소숫점
            priceStr
        }
    }

    fun formatToKoreanWon(value: Long): String {
        return when {
            value >= 1_000_000_000_000 -> {
                "${value / 1_000_000_000_000}조원"
            }

            value >= 100_000_000 -> {
                "${value / 100_000_000}억원"
            }

            value >= 10_000_000 -> {
                "${value / 10_000_000}천만원"
            }

            else -> {
                "${value}원"
            }
        }
    }
}