package com.example.coinnews.model

data class Coin(
    val id: Int,
    val rank: Int?,
    val symbol: String?,
    val name: String,
    val slug: String,
    val usdAsset: Asset?,
)

data class Asset(
    val price: Double?,
    val priceChange24h: Double?,
    val totalMarketCap: Double?
)
