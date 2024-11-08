package com.hong7.coinnews.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

//@Entity(tableName = "coin")
//data class CoinEntity(
//    @PrimaryKey
//    val id: String,
//    val name: String,
//    val symbol: String,
//    val rank: Int
//)

@Entity(tableName = "coin_table")
data class CoinEntity(
    @PrimaryKey
    val marketId: String,
    val koreanName: String,
    val englishName: String
)
