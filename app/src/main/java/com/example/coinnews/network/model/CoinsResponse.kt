package com.example.coinnews.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CoinsResponse(
    @SerialName("data") val items: List<NetworkCoinListItem>,
    val status: NetworkStatus
)

@Serializable
data class NetworkCoinListItem(
    val id: Int,
    val name: String,
    val symbol: String,
    val slug: String,
    @SerialName("cmc_rank") val marketCapRank: Int,
    @SerialName("num_market_pairs") val numMarketPairs: Int?,
    @SerialName("circulating_supply") val circulatingSupply: Double?,
    @SerialName("total_supply") val totalSupply: Double?,
    @SerialName("max_supply") val maxSupply: Double?,
    @SerialName("infinite_supply") val infiniteSupply: Boolean?,
    @SerialName("last_updated") val lastUpdated: String?,
    @SerialName("date_added") val dateAdded: String?,
    val tags: List<String>?,
//    val platform: String?,
    @SerialName("self_reported_circulating_supply") val selfReportedCirculatingSupply: Double?,
    @SerialName("self_reported_market_cap") val selfReportedMarketCap: Double?,
    val quote: Quote
)

@Serializable
data class Quote(
    @SerialName("USD") val usd: NetworkQuoteDetails?,
)

@Serializable
data class NetworkQuoteDetails(
    val price: Double,
    @SerialName("volume_24h") val volume24h: Double,
    @SerialName("volume_change_24h") val volumeChange24h: Double,
    @SerialName("percent_change_1h") val percentChange1h: Double,
    @SerialName("percent_change_24h") val percentChange24h: Double,
    @SerialName("percent_change_7d") val percentChange7d: Double,
    @SerialName("market_cap") val marketCap: Double,
    @SerialName("market_cap_dominance") val marketCapDominance: Double,
    @SerialName("fully_diluted_market_cap") val fullyDilutedMarketCap: Double,
    @SerialName("last_updated") val lastUpdated: String
)
