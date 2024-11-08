//package com.hong7.coinnews.network.derpecated
//
//import com.hong7.coinnews.network.model.response.CoinResponseStatus
//import kotlinx.serialization.SerialName
//import kotlinx.serialization.Serializable
//
//
//@Serializable
//data class GetCategoryListResponse(
//    val data: List<NetworkCategory>,
//    val status: CoinResponseStatus
//)
//
//@Serializable
//data class NetworkCategory(
//    @SerialName("id") val id: String,
//    @SerialName("name") val name: String,
//    @SerialName("title") val title: String,
//    @SerialName("description") val description: String,
//    @SerialName("num_tokens") val numTokens: Int,
//    @SerialName("avg_price_change") val avgPriceChange: Double,
//    @SerialName("market_cap") val marketCap: Double,
//    @SerialName("market_cap_change") val marketCapChange: Double,
//    @SerialName("volume") val volume: Double,
//    @SerialName("volume_change") val volumeChange: Double,
//    @SerialName("last_updated") val lastUpdated: Long
//)