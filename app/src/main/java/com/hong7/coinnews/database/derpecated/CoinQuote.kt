package com.hong7.coinnews.database.derpecated


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
    val selfReportedCirculatingSupply: Double?,
    val selfReportedMarketCap: Double?,
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
    val marketCap: Double?,
    val marketCapDominance: Double?,
    val fullyDilutedMarketCap: Double?,
    val lastUpdated: String?
)