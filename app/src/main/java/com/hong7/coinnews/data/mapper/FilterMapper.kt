package com.hong7.coinnews.data.mapper

import com.hong7.coinnews.database.CoinFilterEntity
import com.hong7.coinnews.database.FilterEntity
import com.hong7.coinnews.model.CoinFilter
import com.hong7.coinnews.model.CountryScope
import com.hong7.coinnews.model.Filter

fun FilterEntity.toDomain(): Filter {
    return Filter(
        id = this.id,
        coinFilters = this.coinFilters.map {
            CoinFilter(
                coinName = it.coinName,
                symbol = it.symbol,
                isSelected = it.isSelected
            )
        },
        scope = if (this.isGlobalSelected) CountryScope.Global else CountryScope.Local
    )
}

fun Filter.toEntity(): FilterEntity {
    return FilterEntity(
        id = this.id,
        coinFilters = this.coinFilters.map {
            CoinFilterEntity(
                coinName = it.coinName,
                symbol = it.symbol,
                isSelected = it.isSelected
            )
        },
        isGlobalSelected = this.scope == CountryScope.Global
    )
}