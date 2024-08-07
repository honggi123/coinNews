package com.hong7.coinnews.data.mapper

import com.hong7.coinnews.database.entity.CoinEntity
import com.hong7.coinnews.model.Coin
import com.hong7.coinnews.network.model.NetworkCoin

fun Coin.toEntity(): CoinEntity {
    return CoinEntity(
        id = this.id,
        name = this.name,
        symbol = this.symbol
    )
}

fun NetworkCoin.toDomain(): Coin {
    return Coin(
        id = this.id,
        name = this.name,
        symbol = this.symbol,
    )
}

fun NetworkCoin.toEntity(): CoinEntity {
    return CoinEntity(
        id = this.id,
        name = this.name,
        symbol = this.symbol,
    )
}
