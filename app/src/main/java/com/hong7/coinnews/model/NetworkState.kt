package com.hong7.coinnews.model

sealed class NetworkState {
    object None: NetworkState()
    object Connected: NetworkState()
    object NotConnected: NetworkState()
}
