package com.hong7.coinnews.data.mapper

import com.hong7.coinnews.database.CoinEntity
import com.hong7.coinnews.model.Coin
import com.hong7.coinnews.network.model.NetworkCoin

fun Coin.toEntity(): CoinEntity {
    return CoinEntity(
        id = this.id,
        name = this.name,
        relatedSearchWord = this.relatedSearchWord,
        symbol = this.symbol
    )
}

fun NetworkCoin.toDomain(): Coin {
    return Coin(
        id = this.id,
        name = this.name,
        symbol = this.symbol,
        relatedSearchWord = this.relatedSearchWords
    )
}

fun NetworkCoin.toEntity(): CoinEntity {
    return CoinEntity(
        id = this.id,
        name = this.name,
        symbol = this.symbol,
        relatedSearchWord = this.relatedSearchWords
    )
}

//fun CoinEntity.toDomain(): Coin {
//    return Coin(
//        id = this.coinId,
//        name = this.name,
//        rank = this.rank,
//        symbol = this.symbol ?: "",
//        slug = this.slug,
//        usdAsset = null,
//        urls = null,
//        description = null
//    )
//}
//
//fun Coin.toEntity(): CoinEntity {
//    return CoinEntity(
//        name = this.name,
//        rank = this.rank,
//        symbol = this.symbol,
//        slug = this.slug,
//        coinId = this.id.toString()
//    )
//}