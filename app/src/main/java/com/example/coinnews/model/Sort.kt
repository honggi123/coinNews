package com.example.coinnews.model

data class Sort(
    val option: SortOption,
    val ordering: Ordering
)

enum class SortOption {
    Rank,
    MarketCap,
    Price,
    PriceChange24h
}

enum class Ordering {
    None,
    Ascending,
    Descending
}
