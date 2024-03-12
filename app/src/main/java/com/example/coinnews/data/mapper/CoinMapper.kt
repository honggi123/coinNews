package com.example.coinnews.data.mapper

import com.example.coinnews.database.CoinEntity
import com.example.coinnews.network.model.NetworkCoinListItem
import com.example.coinnews.model.Asset
import com.example.coinnews.model.Coin
import com.example.coinnews.network.model.NetworkCoinInfo

fun NetworkCoinListItem.toDomain(): Coin {
    return Coin(
        id = this.id,
        name = this.name,
        rank = this.marketCapRank,
        symbol = this.symbol,
        slug = this.slug,
        usdAsset = Asset(
            price = this.quote.usd?.price,
            priceChange24h = this.quote.usd?.percentChange24h,
            totalMarketCap = this.quote.usd?.marketCap
        )
    )
}

fun NetworkCoinInfo.toDomain(): Coin {
    return Coin(
        id = this.id,
        name = this.name,
        rank = null,
        symbol = this.symbol,
        slug = this.slug,
        usdAsset = null
    )
}

fun CoinEntity.toDomain(): Coin {
    return Coin(
        id = this.coinId.toInt(),
        name = this.name,
        rank = this.rank,
        symbol = this.symbol,
        slug = this.slug,
        usdAsset = null
    )
}

fun Coin.toEntity(): CoinEntity {
    return CoinEntity(
        name = this.name,
        rank = this.rank,
        symbol = this.symbol,
        slug = this.slug,
        coinId = this.id.toString()
    )
}