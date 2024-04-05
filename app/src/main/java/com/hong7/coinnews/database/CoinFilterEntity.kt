package com.hong7.coinnews.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "filter")
data class FilterEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,
    @ColumnInfo(name = "coin_filters")
    val coinFilters: List<CoinFilterEntity>,
    @ColumnInfo(name = "is_global_selected")
    val isGlobalSelected: Boolean
)

data class CoinFilterEntity(
    val coinName: String,
    val symbol: String,
    val isSelected: Boolean = false
)