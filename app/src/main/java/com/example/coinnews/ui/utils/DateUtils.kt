package com.example.coinnews.ui.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object DateUtils {

    fun timeToHourMinString(dateTime: LocalDateTime?): String? {
        var timeString: String? = null

        if (dateTime != null) {
            val formatter = DateTimeFormatter.ofPattern("a hh시 mm분")
            timeString = dateTime.format(formatter)
                .replace("AM", "오전")
                .replace("PM", "오후")
        }

        return timeString
    }
}