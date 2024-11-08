package com.hong7.coinnews.ui.feature.market.model

data class Sort(
    val sortCategory: SortCategory,
    val sortType: SortType
)

enum class SortCategory {
    PRICE,
    VOLUME,
    PERCENTAGECHANGE
}

enum class SortType(val order: Int) {
    ASCENDING(3),
    DESCENDING(2),
    NONE(1);

    companion object {
        fun getCurrentSortType(value: Int): SortType {
            return when (value) {
                1 -> NONE
                2 -> DESCENDING
                3 -> ASCENDING
                else -> throw IllegalArgumentException("Invalid value: $value")
            }
        }
    }
}

