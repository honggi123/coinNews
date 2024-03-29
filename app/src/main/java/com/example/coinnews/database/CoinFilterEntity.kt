package com.example.coinnews.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "coin_filter")
data class CoinFilterEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,
    @ColumnInfo(name = "coin_name")
    val coinName: String,
    @ColumnInfo(name = "symbol")
    val symbol: String,
    @ColumnInfo(name = "is_selected")
    val isSelected: Boolean
)