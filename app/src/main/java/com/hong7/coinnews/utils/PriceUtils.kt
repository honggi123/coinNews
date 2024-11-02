package com.hong7.coinnews.utils

object PriceUtils {

    fun getPriceCommaString(value: Double): String {
        return String.format("%,.2f", value)
    }

    fun cutToOneDecimal(value: Double): Double {
        return String.format("%.1f", value).toDouble()
    }
}