package com.hong7.coinnews.model


data class Coin(
    val marketId: String,
    val koreanName: String,
    val englishName: String,
    val tradePrice: Double? = null,
    val changeRate: Double? = null,
    val accTradePrice24h: Double? = null,
)