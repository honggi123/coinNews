package com.hong7.coinnews.model


data class Coin(
    val id: String,
    val name: String,
    val symbol: String,
    val relatedSearchWord: List<String>,
    val isSelected: Boolean = false
)
