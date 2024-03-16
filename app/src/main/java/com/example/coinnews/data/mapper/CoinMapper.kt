package com.example.coinnews.data.mapper

import com.example.coinnews.database.CoinEntity
import com.example.coinnews.network.model.NetworkCoinListItem
import com.example.coinnews.model.Asset
import com.example.coinnews.model.Coin
import com.example.coinnews.model.UrlType
import com.example.coinnews.network.model.NetworkCoinInfo
import kotlinx.collections.immutable.toImmutableMap

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
        ),
        urls = null,
        description = null
    )
}

fun NetworkCoinInfo.toDomain(): Coin {
    return Coin(
        id = this.id,
        name = this.name,
        rank = null,
        symbol = this.symbol,
        slug = this.slug,
        usdAsset = null,
        description = this.description,
        urls = mapOf(
            Pair(UrlType.Website, this.urls.websiteUrls.firstOrNull()),
            Pair(UrlType.Reddit, this.urls.redditUrls.firstOrNull()),
            Pair(UrlType.Gtihub, this.urls.sourceCode.firstOrNull())
        ).toImmutableMap()
    )
}

fun CoinEntity.toDomain(): Coin {
    return Coin(
        id = this.coinId.toInt(),
        name = this.name,
        rank = this.rank,
        symbol = this.symbol,
        slug = this.slug,
        usdAsset = null,
        urls = null,
        description = null
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