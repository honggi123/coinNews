package com.hong7.coinnews.model.exception

data class NetworkDisconnectedException(
    override val message: String = "네트워크가 연결이 되어있지 않습니다."
) : Exception()
