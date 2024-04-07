package com.hong7.coinnews.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class Converter {
    @TypeConverter
    fun localDateTimeToTimestamp(dateTime: LocalDateTime): Long =
        dateTime.atZone(ZoneId.systemDefault()).toEpochSecond()

    @TypeConverter
    fun timestampToLocalDateTime(value: Long): LocalDateTime =
        Instant.ofEpochSecond(value).atZone(ZoneId.systemDefault()).toLocalDateTime()

    @TypeConverter
    fun listToJson(value: List<CoinFilterEntity>?): String? {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonToList(value: String): List<CoinFilterEntity>? {
        return Gson().fromJson(value,Array<CoinFilterEntity>::class.java)?.toList()
    }
}