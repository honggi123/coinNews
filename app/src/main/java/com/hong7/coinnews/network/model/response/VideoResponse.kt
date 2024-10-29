package com.hong7.coinnews.network.model.response

import kotlinx.serialization.Serializable

@Serializable
data class VideoListResponse(
    val kind: String,
    val etag: String,
    val nextPageToken: String? = null,
    val prevPageToken: String? = null,
    val regionCode: String,
    val pageInfo: NetworkPageInfo,
    val items: List<NetworkSearchResultItem>
)

@Serializable
data class NetworkPageInfo(
    val totalResults: Int,
    val resultsPerPage: Int
)

@Serializable
data class NetworkSearchResultItem(
    val kind: String,
    val etag: String,
    val id: NetworkResourceId,
    val snippet: NetworkSearchSnippet
)

@Serializable
data class NetworkResourceId(
    val kind: String,
    val videoId: String? = null,
    val channelId: String? = null,
    val playlistId: String? = null
)

@Serializable
data class NetworkSearchSnippet(
    val publishedAt: String,
    val channelId: String,
    val title: String,
    val description: String,
    val thumbnails: Map<String, NetworkThumbnailInfo>,
    val channelTitle: String,
    val liveBroadcastContent: String
)

@Serializable
data class NetworkThumbnailInfo(
    val url: String,
    val width: Int? = null,
    val height: Int? = null
)