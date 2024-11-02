package com.hong7.coinnews.data.mapper

import com.hong7.coinnews.network.model.response.NetworkNews
import com.hong7.coinnews.model.News
import com.hong7.coinnews.utils.DateUtils
import com.hong7.coinnews.utils.NumberUtils.getHashValue
import java.time.LocalDateTime
import java.util.Locale

fun NetworkNews.toDomain(): News {
    return News(
        id = getHashValue(this.title),
        title = this.title.replaceHtmlTags(),
        url = this.url,
        description = this.description.replaceHtmlTags(),
        author = parseDomain(this.originalUrl),
        createdAt = DateUtils.formatDateTimeWithTimeZoneName(this.createdAt)
    )
}

private fun parseDomain(url: String): String? {
    val regex = Regex("(?:https?://)?(?:www\\.)?([a-zA-Z0-9-]+)\\.[a-zA-Z]{2,6}")
    val matchResult = regex.find(url)
    return matchResult?.groupValues?.get(1)?.uppercase(Locale.ROOT)
}


private fun String.replaceHtmlTags(): String {
    return this.replace(Regex("<.*?>"), "")
        .replace("&quot;", "\"")
        .replace("&amp;", "&")
        .replace("&lt;", "<")
        .replace("&gt;", ">")
}

