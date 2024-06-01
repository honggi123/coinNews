package com.hong7.coinnews.ui.model

import androidx.compose.runtime.Stable
import com.hong7.coinnews.model.Article
import java.time.LocalDateTime

@Stable
data class StableLocalDateTime(
    val time: LocalDateTime
)

//data class ArticleUiModel(
//    val id: String,
//    val title: String,
//    val description: String = "",
//    val url: String,
//    val author: String? = null,
//    val createdAt: StableLocalDateTime? = null
//)

//data class ArticleUiModelWithInterest(
//    val article: ArticleUiModel,
//    val isInterested: Boolean
//)