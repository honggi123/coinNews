package com.hong7.coinnews.network.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MinutesCandleResponse(
    @SerialName("market") val market: String,
    @SerialName("candle_date_time_utc") val candleDateTimeUtc: String,
    @SerialName("candle_date_time_kst") val candleDateTimeKst: String,
    @SerialName("opening_price") val openingPrice: Double,
    @SerialName("high_price") val highPrice: Double,
    @SerialName("low_price") val lowPrice: Double,
    @SerialName("trade_price") val tradePrice: Double,
    @SerialName("timestamp") val timestamp: Long,
    @SerialName("candle_acc_trade_price") val candleAccTradePrice: Double,
    @SerialName("candle_acc_trade_volume") val candleAccTradeVolume: Double,
)

@Serializable
data class DaysCandleResponse(
    @SerialName("market") val market: String,
    @SerialName("candle_date_time_utc") val candleDateTimeUtc: String,
    @SerialName("candle_date_time_kst") val candleDateTimeKst: String,
    @SerialName("opening_price") val openingPrice: Double,
    @SerialName("high_price") val highPrice: Double,
    @SerialName("low_price") val lowPrice: Double,
    @SerialName("trade_price") val tradePrice: Double,
    @SerialName("timestamp") val timestamp: Long,
    @SerialName("candle_acc_trade_price") val candleAccTradePrice: Double,
    @SerialName("change_price") val changePrice: Double?,
    @SerialName("change_rate") val changeRate: Double,
)