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

data class Filter(
    val id: Int = 0,
    val coinFilters: List<CoinFilter>,
    val scope: CountryScope
)

data class CoinFilter(
    val id: String? = null,
    val coinName: String,
    val symbol: String,
    val isSelected: Boolean = false
)

enum class CountryScope {
    Local,
    Global
}