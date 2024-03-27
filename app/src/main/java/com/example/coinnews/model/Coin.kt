package com.example.coinnews.model

import kotlinx.collections.immutable.ImmutableMap

data class Coin(
    val id: String,
    val rank: Int? = null,
    val symbol: String,
    val name: String,
    val description: String? = null,
    val slug: String,
    val usdAsset: CoinAsset? = null,
    val urls: ImmutableMap<UrlType, String?>? = null,
)

data class CoinAsset(
    val price: Double?,
    val priceChange24h: Double?,
    val totalMarketCap: Double?
)

enum class CoinFilter(
    val coinName: String,
    val symbol: String,
    val slug: String
) {
    Bitcoin("비트코인", "BTC", ""),
    Ethereum("이더리움", "ETC", "")
}
