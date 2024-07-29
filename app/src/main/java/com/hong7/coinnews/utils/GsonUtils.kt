package com.hong7.coinnews.utils

import com.google.gson.GsonBuilder
import com.google.gson.JsonParseException
import com.google.gson.*
import java.lang.reflect.Type
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object GsonUtils {

    val gson = GsonBuilder().disableHtmlEscaping()
        .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
        .create()

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

class LocalDateTimeAdapter : JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    override fun serialize(
        src: LocalDateTime,
        typeOfSrc: Type,
        context: JsonSerializationContext
    ): JsonElement {
        return JsonPrimitive(src.format(formatter))
    }

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): LocalDateTime {
        return LocalDateTime.parse(json.asString, formatter)
    }
}