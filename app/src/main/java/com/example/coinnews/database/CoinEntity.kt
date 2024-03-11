package com.example.coinnews.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Calendar

@Entity(tableName = "coin_interested")
data class CoinEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,
    @ColumnInfo(name = "coin_id")
    val coinId: String,
    @ColumnInfo(name = "rank")
    val rank: Int?,
    @ColumnInfo(name = "symbol")
    val symbol: String?,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "slug")
    val slug: String,
    @ColumnInfo(name = "put_date")
    val putDate: Calendar = Calendar.getInstance(),
    // toto add asset
)