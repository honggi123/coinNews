package com.hong7.coinnews.data.mapper

import com.hong7.coinnews.database.entity.CoinEntity
import com.hong7.coinnews.model.Coin
import com.hong7.coinnews.network.model.response.NetworkCoin

fun Coin.toEntity(): CoinEntity {
    return CoinEntity(
        id = this.id,
        name = this.name,
        symbol = this.symbol,
        rank = this.rank
    )
}

fun NetworkCoin.toDomain(): Coin {
    return Coin(
        id = this.id.toString(),
        name = this.name,
        symbol = this.symbol,
        rank = this.rank
    )
}

fun NetworkCoin.toEntity(): CoinEntity {
    return CoinEntity(
        id = this.id.toString(),
        name = this.name,
        symbol = this.symbol,
        rank = this.rank
    )
}

fun CoinEntity.toDomain(): Coin {
    return Coin(
        id = this.id,
        name = this.name,
        symbol = this.symbol,
        rank = this.rank
    )
}

