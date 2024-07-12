package com.hong7.coinnews.database

data class CoinEntity(
    val id: String,
    val name: String,
    val symbol: String,
    val isSelected: Boolean = false
)