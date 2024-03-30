package com.example.coinnews.database

import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Calendar

class Converter {
    @TypeConverter
    fun localDateTimeToTimestamp(dateTime: LocalDateTime): Long =
        dateTime.atZone(ZoneId.systemDefault()).toEpochSecond()

    @TypeConverter
    fun timestampToLocalDateTime(value: Long): LocalDateTime =
        Instant.ofEpochSecond(value).atZone(ZoneId.systemDefault()).toLocalDateTime()
}
