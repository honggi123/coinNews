package com.hong7.coinnews.data.mapper

import com.hong7.coinnews.model.ResourceId
import com.hong7.coinnews.model.SearchSnippet
import com.hong7.coinnews.model.ThumbnailInfo
import com.hong7.coinnews.model.VideoItem
import com.hong7.coinnews.network.model.response.NetworkResourceId
import com.hong7.coinnews.network.model.response.NetworkSearchResultItem
import com.hong7.coinnews.network.model.response.NetworkSearchSnippet
import com.hong7.coinnews.network.model.response.NetworkThumbnailInfo
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun NetworkSearchResultItem.toDomain(): VideoItem {
    return VideoItem(
        kind = this.kind,
        etag = this.etag,
        id = this.id.toDomain(),
        snippet = this.snippet.toDomain()
    )
}

fun NetworkResourceId.toDomain(): ResourceId {
    return ResourceId(
        kind = this.kind,
        videoId = this.videoId,
        channelId = this.channelId,
        playlistId = this.playlistId
    )
}

fun NetworkSearchSnippet.toDomain(): SearchSnippet {
    return SearchSnippet(
        publishedAt = this.publishedAt.toLocalDateTime(),
        channelId = this.channelId,
        title = this.title,
        description = this.description,
        thumbnails = this.thumbnails.mapValues { it.value.toLocal() },
        channelTitle = this.channelTitle,
        liveBroadcastContent = this.liveBroadcastContent
    )
}

fun NetworkThumbnailInfo.toLocal(): ThumbnailInfo {
    return ThumbnailInfo(
        url = this.url,
        width = this.width,
        height = this.height
    )
}

fun String.toLocalDateTime(): LocalDateTime {
    val formatter = DateTimeFormatter.ISO_DATE_TIME
    return LocalDateTime.parse(this, formatter)
}