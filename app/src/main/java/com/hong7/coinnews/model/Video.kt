package com.hong7.coinnews.model

import java.time.LocalDateTime

data class VideoItem(
    val kind: String,
    val etag: String,
    val id: String,
    val snippet: SearchSnippet,
)

data class ResourceId(
    val kind: String,
    val videoId: String? = null,
)

data class SearchSnippet(
    val publishedAt: LocalDateTime,
    val channelId: String,
    val title: String,
    val description: String,
    val thumbnails: Map<String, ThumbnailInfo>,
    val channelTitle: String,
    val resourceId: ResourceId
)

data class ThumbnailInfo(
    val url: String,
    val width: Int? = null,
    val height: Int? = null
)