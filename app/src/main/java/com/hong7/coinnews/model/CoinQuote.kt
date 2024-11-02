package com.hong7.coinnews.model

import java.math.BigDecimal


data class CoinQuote(
    val id: Int,
    val name: String,
    val symbol: String,
    val slug: String,
    val isActive: Int?,
    val isFiat: Int?,
    val circulatingSupply: Double?,
    val totalSupply: Double?,
    val maxSupply: Double?,
    val dateAdded: String?,
    val numMarketPairs: Int?,
    val cmcRank: Int?,
    val lastUpdated: String?,
    val tags: List<Tag>?,
    val selfReportedCirculatingSupply: Long?,
    val selfReportedMarketCap: Long?,
    val quote: Map<String, Asset>?
)

data class Tag(
    val slug: String?,
    val name: String?,
    val category: String?
)

data class Asset(
    val price: Double?,
    val volume24h: Double?,
    val volumeChange24h: Double?,
    val percentChange1h: Double?,
    val percentChange24h: Double?,
    val percentChange7d: Double?,
    val percentChange30d: Double?,
    val marketCap: BigDecimal?,
    val marketCapDominance: Double?,
    val fullyDilutedMarketCap: Double?,
    val lastUpdated: String?
)