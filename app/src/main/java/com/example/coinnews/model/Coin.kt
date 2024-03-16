package com.example.coinnews.model

import kotlinx.collections.immutable.ImmutableMap

data class Coin(
    val id: Int,
    val rank: Int? = null,
    val symbol: String? = null,
    val name: String,
    val description: String? = null,
    val slug: String,
    val usdAsset: Asset? = null,
    val urls: ImmutableMap<UrlType, String?>? = null,
)

data class Asset(
    val price: Double?,
    val priceChange24h: Double?,
    val totalMarketCap: Double?
)
