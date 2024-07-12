package com.hong7.coinnews.utils

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoUnit
import java.util.Locale

object DateUtils {

    fun getTimeAgo(
        fromTime: LocalDateTime,
        toTime: LocalDateTime = LocalDateTime.now()
    ): String {
        val minutes = ChronoUnit.MINUTES.between(fromTime, toTime)
        val hours = ChronoUnit.HOURS.between(fromTime, toTime)
        val days = ChronoUnit.DAYS.between(fromTime, toTime)
        val months = ChronoUnit.MONTHS.between(fromTime, toTime)
        val years = ChronoUnit.YEARS.between(fromTime, toTime)

        return when {
            minutes < 60 -> "${minutes}분전"
            hours < 24 -> "${hours}시간전"
            days < 31 -> "${days}일전"
            months < 12 -> "${months}개월전"
            else -> "${years}년전"
        }
    }


    fun formatDateTimeWithTimeZoneName(dateTimeString: String): LocalDateTime {
        val formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US)
        return LocalDateTime.parse(dateTimeString, formatter)
    }

    fun formatDateTimeWithUtcOffset(dateTimeString: String): LocalDateTime {
        val formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss z", Locale.US)
        return LocalDateTime.parse(dateTimeString, formatter)
    }
}