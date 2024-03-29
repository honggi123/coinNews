package com.example.coinnews.model

import kotlinx.collections.immutable.ImmutableMap

data class Coin(
    val id: String,
    val symbol: String,
    val name: String,
    val slug: String,
    val rank: Int? = null,
    val description: String? = null,
    val usdAsset: CoinAsset? = null,
    val urls: ImmutableMap<UrlType, String?>? = null,
)

data class CoinAsset(
    val price: Double?,
    val priceChange24h: Double?,
    val totalMarketCap: Double?
)


