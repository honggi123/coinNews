package com.example.coinnews.model

data class Article(
    val id: String,
    val title: String,
    val imageUrl: String,
    val url: String,
    val assets: List<ArticleAsset>,
    val metaData: ArticleMetaData
)

data class ArticleMetaData(
    val author: String,
    val createdAt: String,
)

data class ArticleAsset(
    val id: String,
    val name: String,
    val symbol: String
)