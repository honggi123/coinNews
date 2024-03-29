package com.example.coinnews.model
data class CoinFilter(
    val id: String? = null,
    val coinName: String,
    val symbol: String,
    val isSelected: Boolean = false
)