package com.hong7.coinnews.model

import java.time.LocalDateTime

data class VideoItem(
    val kind: String,
    val etag: String,
    val id: ResourceId,
    val snippet: SearchSnippet
)

data class ResourceId(
    val kind: String,
    val videoId: String? = null,
    val channelId: String? = null,
    val playlistId: String? = null
)

data class SearchSnippet(
    val publishedAt: LocalDateTime,
    val channelId: String,
    val title: String,
    val description: String,
    val thumbnails: Map<String, ThumbnailInfo>,
    val channelTitle: String,
    val liveBroadcastContent: String
)

data class ThumbnailInfo(
    val url: String,
    val width: Int? = null,
    val height: Int? = null
)