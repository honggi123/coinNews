package com.hong7.coinnews.utils

import java.security.MessageDigest

object NumberUtils {

    fun formatDoubleWithUnit(value: Double?, unit: String = ""): String? {
        return value?.let { String.format("%.2f$unit", it) }
    }

    fun getHashValue(value: String): String {
        val bytes = value.toByteArray()
        val digest = MessageDigest.getInstance("SHA-256").digest(bytes)
        return digest.fold("", { str, it -> str + "%02x".format(it) })
    }
}
