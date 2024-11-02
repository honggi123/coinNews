package com.hong7.coinnews.data.mapper

import com.hong7.coinnews.database.entity.CoinEntity
import com.hong7.coinnews.database.entity.WatchListCoinEntity
import com.hong7.coinnews.model.Asset
import com.hong7.coinnews.model.Coin
import com.hong7.coinnews.model.CoinQuote
import com.hong7.coinnews.model.Tag
import com.hong7.coinnews.network.model.response.NetworkAsset
import com.hong7.coinnews.network.model.response.NetworkCoin
import com.hong7.coinnews.network.model.response.NetworkCoinQuote
import com.hong7.coinnews.network.model.response.NetworkTag

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

fun Coin.toWatchListEntity(): WatchListCoinEntity {
    return WatchListCoinEntity(
        id = this.id,
        name = this.name,
        symbol = this.symbol,
        rank = this.rank
    )
}

fun WatchListCoinEntity.toDomain(): Coin {
    return Coin(
        id = this.id,
        name = this.name,
        symbol = this.symbol,
        rank = this.rank
    )
}


fun NetworkCoinQuote.toDomain(): CoinQuote {
    return CoinQuote(
        id = this.id,
        name = this.name,
        symbol = this.symbol,
        slug = this.slug,
        isActive = this.isActive,
        isFiat = this.isFiat,
        circulatingSupply = this.circulatingSupply,
        totalSupply = this.totalSupply,
        maxSupply = this.maxSupply,
        dateAdded = this.dateAdded,
        numMarketPairs = this.numMarketPairs,
        cmcRank = this.cmcRank,
        lastUpdated = this.lastUpdated,
        tags = this.tags?.map { it.toDomain() },
        selfReportedCirculatingSupply = this.selfReportedCirculatingSupply,
        selfReportedMarketCap = this.selfReportedMarketCap,
        quote = this.quote?.mapValues {
            it.value.toDomain()
        }
    )
}

fun NetworkTag.toDomain(): Tag{
    return Tag(
        slug = this.slug,
        name = this.name,
        category = this.category
    )
}

fun NetworkAsset.toDomain(): Asset {
    return Asset(
        price = this.price,
        volume24h = this.volume24h,
        volumeChange24h = this.volumeChange24h,
        percentChange1h = this.percentChange1h,
        percentChange24h = this.percentChange24h,
        percentChange7d = this.percentChange7d,
        percentChange30d = this.percentChange30d,
        marketCap = this.marketCap,
        marketCapDominance = this.marketCapDominance,
        fullyDilutedMarketCap = this.fullyDilutedMarketCap,
        lastUpdated = this.lastUpdated
    )
}