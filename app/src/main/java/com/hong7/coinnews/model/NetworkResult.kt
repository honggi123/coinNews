package com.hong7.coinnews.model

sealed class NetworkResult<out DATA> {
    data class Success<DATA>(var data : DATA) : NetworkResult<DATA>()
    data class Fail(val message : String) : NetworkResult<Nothing>()
    data class Exception(val exception: java.lang.Exception) : NetworkResult<Nothing>()
}

suspend fun <T> networkHandling(block : suspend () -> T) : NetworkResult<T> {
    return try {
        NetworkResult.Success(block())
    } catch (e : Exception){
        NetworkResult.Exception(e)
    }
}

fun <T> NetworkResult<T>.toModel() : T =
    (this as NetworkResult.Success).data


suspend fun <T,R> NetworkResult<T>.mapNetworkResult(getData : suspend (T) -> R) : NetworkResult<R>{
    return changeNetworkData(getData(toModel()))
}

private fun <R> changeNetworkData(replaceData: R): NetworkResult<R> {
    return NetworkResult.Success(replaceData)
}


