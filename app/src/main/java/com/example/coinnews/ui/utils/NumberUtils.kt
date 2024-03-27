package com.example.coinnews.ui.utils

object NumberUtils {

    fun formatDoubleWithUnit(value: Double?, unit: String = ""): String? {
        return value?.let { String.format("%.2f$unit", it) }
    }
}
