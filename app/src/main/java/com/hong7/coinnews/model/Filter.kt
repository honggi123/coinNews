package com.hong7.coinnews.model

enum class Ordering {
    None,
    Ascending,
    Descending
}

data class Filter(
    val id: Int = 0,
    val coins: List<Coin>,
)
