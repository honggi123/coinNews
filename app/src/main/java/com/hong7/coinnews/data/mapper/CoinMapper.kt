package com.hong7.coinnews.data.mapper

import com.hong7.coinnews.database.entity.CoinEntity
import com.hong7.coinnews.model.Coin
import com.hong7.coinnews.model.CoinPrice
import com.hong7.coinnews.network.model.NetworkCoin
import com.hong7.coinnews.network.model.NetworkCoinPrice

fun Coin.toEntity(): CoinEntity {
    return CoinEntity(
        marketId = this.marketId,
        koreanName = this.koreanName,
        englishName = this.englishName
    )
}

fun NetworkCoin.toDomain(): Coin {
    return Coin(
        marketId = this.marketId,
        koreanName = this.koreanName,
        englishName = this.englishName,
    )
}

fun NetworkCoin.toEntity(): CoinEntity {
    return CoinEntity(
        marketId = this.marketId,
        koreanName = this.koreanName,
        englishName = this.englishName
    )
}

fun CoinEntity.toDomain(): Coin {
    return Coin(
        marketId = this.marketId,
        koreanName = this.koreanName,
        englishName = this.englishName
    )
}

fun NetworkCoinPrice.toDomain(): CoinPrice {
    return CoinPrice(
        market = this.market,
        tradePrice = this.tradePrice,
        changeRate = this.signedChangeRate,
        accTradePrice24h = this.accTradePrice24h,
    )
}
