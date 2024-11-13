package com.hong7.coinnews.data.mapper

import com.hong7.coinnews.model.ResourceId
import com.hong7.coinnews.model.SearchSnippet
import com.hong7.coinnews.model.ThumbnailInfo
import com.hong7.coinnews.model.VideoItem
import com.hong7.coinnews.network.model.response.NetworkContentDetails
import com.hong7.coinnews.network.model.response.NetworkPlaylistItem
import com.hong7.coinnews.network.model.response.NetworkResourceId
import com.hong7.coinnews.network.model.response.NetworkSnippet
import com.hong7.coinnews.network.model.response.NetworkThumbnail
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun NetworkPlaylistItem.toDomain(): VideoItem {
    return VideoItem(
        kind = this.kind,
        etag = this.etag,
        id = this.id,
        snippet = this.snippet.toDomain(),
    )
}

fun NetworkSnippet.toDomain(): SearchSnippet {
    return SearchSnippet(
        publishedAt = this.publishedAt.toLocalDateTime(),
        channelId = this.channelId,
        title = this.title,
        description = this.description,
        thumbnails = this.thumbnails.mapValues { it.value.toDomain() },
        channelTitle = this.channelTitle,
        resourceId = this.resourceId.toDomain()
    )
}


fun NetworkThumbnail.toDomain(): ThumbnailInfo {
    return ThumbnailInfo(
        url = this.url,
        width = this.width,
        height = this.height
    )
}

fun NetworkResourceId.toDomain(): ResourceId{
    return ResourceId(
        kind = this.kind,
        videoId = this.videoId,
    )
}

fun String.toLocalDateTime(): LocalDateTime {
    val formatter = DateTimeFormatter.ISO_DATE_TIME
    return LocalDateTime.parse(this, formatter)
}