package com.example.coinnews.model

data class Article(
    val id: String,
    val title: String,
    val imageUrl: String,
    val url: String,
    val metaData: ArticleMetaData
)

data class ArticleMetaData(
    val author: String,
    val postDate: String,
)