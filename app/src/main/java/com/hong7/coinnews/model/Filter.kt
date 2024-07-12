package com.hong7.coinnews.model

import kotlinx.collections.immutable.ImmutableList

data class Filter(
    val id: Int = 0,
    val coins: ImmutableList<Coin>,
)
