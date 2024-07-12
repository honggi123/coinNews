package com.hong7.coinnews.network.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class NetworkCoin(
    @DocumentId
    val id: String = "",
    val name: String = "",
    val symbol: String = "",
    val relatedSearchWords: List<String> = emptyList(),
    val updatedAt: Timestamp? = null,
)
