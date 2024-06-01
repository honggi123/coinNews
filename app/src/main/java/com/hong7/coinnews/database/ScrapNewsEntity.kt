package com.hong7.coinnews.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "scrap_news")
data class ScrapNewsEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,
    @ColumnInfo(name = "news_id")
    var newsId: String,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "url")
    val url: String,
    @ColumnInfo(name = "author_name")
    val authorName: String? = "",
    @ColumnInfo(name = "put_date")
    val createdAt: LocalDateTime,
)