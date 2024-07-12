package com.hong7.coinnews.utils

import com.google.gson.GsonBuilder
import com.google.gson.JsonParseException

object GsonUtils {

    val gson = GsonBuilder().disableHtmlEscaping().create()

    fun toJson(value: Any?) : String {
        return gson.toJson(value)
    }

    inline fun <reified T> fromJson(value: String?) : T? {
        return try {
            gson.fromJson(value, T::class.java)
        } catch (e: JsonParseException) {
            null
        }
    }
}