package com.example.coinnews.ui.utils

import android.util.Log
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoUnit
import java.util.Locale

object DateUtils {

    fun timeStringToTimestamp(timeString: String): Long? {
        return try {
            val formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss Z")
            val zonedDateTime = ZonedDateTime.parse(timeString, formatter)
            zonedDateTime.toEpochSecond() * 1000
        } catch (e: DateTimeParseException) {
            null
        }
    }

    fun getTimeAgo(timestamp: Long): String {
        val instant = Instant.ofEpochMilli(timestamp)
        val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return localDateTime.format(formatter)
    }

    fun timeStampToLocalDateTime(value: Long): LocalDateTime {
        return Instant.ofEpochSecond(value).atZone(ZoneId.systemDefault()).toLocalDateTime()
    }

    fun localDateTimeToTimeStamp(dateTime: LocalDateTime): Long {
        return dateTime.atZone(ZoneId.systemDefault()).toEpochSecond()
    }
}