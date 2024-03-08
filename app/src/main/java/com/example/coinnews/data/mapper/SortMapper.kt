package com.example.coinnews.data.mapper

import com.example.coinnews.model.Ordering
import com.example.coinnews.model.SortOption

fun Ordering.toNetwork(): String {
    return when (this) {
        Ordering.Ascending -> "asc"
        Ordering.Descending -> "desc"
        Ordering.None -> "" // todo
    }
}

fun SortOption.toNetwork(): String {
    return when (this) {
        SortOption.MarketCap -> "market_cap"
        SortOption.Price -> "price"
        SortOption.PriceChange24h -> "percent_change_24h"
        SortOption.Rank -> "rank"
    }
}