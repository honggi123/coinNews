package com.hong7.coinnews.network.model.response

import kotlinx.serialization.Serializable

@Serializable
data class VideoListResponse(
    val kind: String,
    val etag: String,
    val nextPageToken: String? = null,
    val prevPageToken: String? = null,
    val pageInfo: NetworkPageInfo,
    val items: List<NetworkPlaylistItem>
)

@Serializable
data class NetworkPageInfo(
    val totalResults: Int,
    val resultsPerPage: Int
)
@Serializable
data class NetworkPlaylistItem(
    val kind: String,
    val etag: String,
    val id: String,
    val snippet: NetworkSnippet,
)

@Serializable
data class NetworkSnippet(
    val publishedAt: String,
    val channelId: String,
    val title: String,
    val description: String,
    val thumbnails: Map<String, NetworkThumbnail>,
    val channelTitle: String,
    val videoOwnerChannelTitle: String,
    val videoOwnerChannelId: String,
    val playlistId: String,
    val position: Int,
    val resourceId: NetworkResourceId
)

@Serializable
data class NetworkThumbnail(
    val url: String,
    val width: Int? = null,
    val height: Int? = null
)

@Serializable
data class NetworkResourceId(
    val kind: String,
    val videoId: String
)

@Serializable
data class NetworkContentDetails(
    val videoId: String,
    val startAt: String? = null,
    val endAt: String? = null,
    val note: String? = null,
    val videoPublishedAt: String
)

@Serializable
data class NetworkStatus(
    val privacyStatus: String
)