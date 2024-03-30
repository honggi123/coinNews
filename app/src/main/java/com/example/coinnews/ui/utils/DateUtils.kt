package com.example.coinnews.ui.utils

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object DateUtils {

    fun timeStringToTimestamp(timeString: String): Long {
        val formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss Z")
        val zonedDateTime = ZonedDateTime.parse(timeString, formatter)
        return zonedDateTime.toInstant().toEpochMilli()
    }

    fun timestampToAmPmTimeString(timestamp: Long): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy년 M월 d일 a h시 m분")
        val localDateTime = LocalDateTime.ofInstant(
            java.time.Instant.ofEpochMilli(timestamp),
            java.util.TimeZone.getDefault().toZoneId()
        )
        return formatter.format(localDateTime).replace("AM", "오전").replace("PM", "오후")
    }

    fun timeStampToLocalDateTime(value: Long): LocalDateTime {
        return Instant.ofEpochSecond(value).atZone(ZoneId.systemDefault()).toLocalDateTime()
    }

    fun localDateTimeToTimeStamp(dateTime: LocalDateTime): Long {
        return dateTime.atZone(ZoneId.systemDefault()).toEpochSecond()
    }
}