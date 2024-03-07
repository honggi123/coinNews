package com.example.coinnews.data.mapper

import com.example.coinnews.model.CoinSortOption
import com.example.coinnews.model.Sort


fun CoinSortOption.toNetwork(): String {
    return when(this){
        CoinSortOption.MarketCap -> "market_cap"
        CoinSortOption.Price -> "price"
        CoinSortOption.PriceChange24h -> "percent_change_24h"
        CoinSortOption.Rank -> "rank"
    }
}

fun Sort.toNetwork(): String {
    return when(this){
        Sort.Ascending -> "asc"
        Sort.Descending -> "desc"
        Sort.None -> "" // todo
    }
}