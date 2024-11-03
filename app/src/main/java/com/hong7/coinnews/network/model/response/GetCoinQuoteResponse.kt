package com.hong7.coinnews.network.model.response

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.math.BigDecimal

@Serializable
data class GetCoinQuoteResponse(
    val data: Map<String, NetworkCoinQuote>,
    val status: CoinResponseStatus
)

@Serializable
data class NetworkCoinQuote(
    val id: Int,
    val name: String,
    val symbol: String,
    val slug: String,
    @SerialName("is_active") val isActive: Int?,
    @SerialName("is_fiat") val isFiat: Int?,
    @SerialName("circulating_supply") val circulatingSupply: Double,
    @SerialName("total_supply") val totalSupply: Double,
    @SerialName("max_supply") val maxSupply: Double?,
    @SerialName("date_added") val dateAdded: String?,
    @SerialName("num_market_pairs") val numMarketPairs: Int?,
    @SerialName("cmc_rank") val cmcRank: Int?,
    @SerialName("last_updated") val lastUpdated: String?,
    val tags: List<NetworkTag>?,
    @SerialName("self_reported_circulating_supply") val selfReportedCirculatingSupply: Double?,
    @SerialName("self_reported_market_cap") val selfReportedMarketCap: Double?,
    val quote: Map<String, NetworkAsset>?
)

@Serializable
data class NetworkTag(
    val slug: String,
    val name: String,
    val category: String
)

@Serializable
data class NetworkAsset(
    val price: Double?,
    @SerialName("volume_24h") val volume24h: Double?,
    @SerialName("volume_change_24h") val volumeChange24h: Double?,
    @SerialName("percent_change_1h") val percentChange1h: Double?,
    @SerialName("percent_change_24h") val percentChange24h: Double?,
    @SerialName("percent_change_7d") val percentChange7d: Double?,
    @SerialName("percent_change_30d") val percentChange30d: Double?,
    @SerialName("market_cap") val marketCap: Double?,
    @SerialName("market_cap_dominance") val marketCapDominance: Double?,
    @SerialName("fully_diluted_market_cap") val fullyDilutedMarketCap: Double?,
    @SerialName("last_updated") val lastUpdated: String?
)