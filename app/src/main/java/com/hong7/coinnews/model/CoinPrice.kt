package com.hong7.coinnews.model

data class CoinPrice(
    val market: String,
    val tradePrice: Double? = null,
    val changeRate: Double? = null,
    val accTradePrice24h: Double? = null,
)
