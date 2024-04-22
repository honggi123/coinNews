package com.hong7.coinnews.data.mapper

import com.hong7.coinnews.database.CoinEntity
import com.hong7.coinnews.database.FilterEntity
import com.hong7.coinnews.model.Coin
import com.hong7.coinnews.model.Filter

fun FilterEntity.toDomain(): Filter {
    return Filter(
        id = this.id,
        coins = this.coins.map {
            Coin(
                id = it.id,
                name = it.name,
                symbol = it.symbol,
                isSelected = it.isSelected,
                relatedSearchWord = it.relatedSearchWord
            )
        },
    )
}

fun Filter.toEntity(): FilterEntity {
    return FilterEntity(
        id = this.id,
        coins = this.coins.map {
            CoinEntity(
                id = it.id,
                name = it.name,
                symbol = it.symbol,
                isSelected = it.isSelected,
                relatedSearchWord = it.relatedSearchWord
            )
        },
    )
}