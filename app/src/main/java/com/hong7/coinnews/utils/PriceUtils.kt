package com.hong7.coinnews.utils

object PriceUtils {

    fun roundAfterMultiplyingBy100(value: Double): Double {
        return (value * 10_000).let { Math.round(it) / 100.0 }
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