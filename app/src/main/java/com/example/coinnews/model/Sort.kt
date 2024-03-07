package com.example.coinnews.model

enum class Sort(val step: Int) {
    None(0),
    Ascending(1),
    Descending(2);

    companion object {
        fun find(step: Int): Sort = values().find { it.step == step } ?: None
    }
}