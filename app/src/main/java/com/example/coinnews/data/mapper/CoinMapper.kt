package com.example.coinnews.data.mapper

import android.net.Network
import com.example.coinnews.data.network.model.NetworkCoin
import com.example.coinnews.model.Asset
import com.example.coinnews.model.Coin

fun NetworkCoin.toDomain(): Coin {
   return Coin(
        id = this.id,
        name = this.name,
        rank = this.cmcRank,
        symbol = this.symbol,
        usdAsset = Asset(
            price = this.quote.usd?.price,
            priceChange24h = this.quote.usd?.percentChange24h,
            totalMarketCap = this.quote.usd?.marketCap
        )
    )
}