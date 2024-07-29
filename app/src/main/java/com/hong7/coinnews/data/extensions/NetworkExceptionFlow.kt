package com.hong7.coinnews.data.extensions

import com.hong7.coinnews.model.exception.AppException
import com.hong7.coinnews.model.exception.ClientException
import com.hong7.coinnews.model.exception.NetworkException
import com.hong7.coinnews.model.exception.ResponseResource
import com.hong7.coinnews.model.exception.ServerException
import com.hong7.coinnews.model.exception.UnknownException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import retrofit2.HttpException
import java.io.IOException

fun <T> Flow<T>.asResponseResourceFlow(): Flow<ResponseResource<T>> {
    return this
        .map<T, ResponseResource<T>> {
            ResponseResource.Success(it)
        }
        .onStart { emit(ResponseResource.Loading(true)) }
        .onCompletion { emit(ResponseResource.Loading(false)) }
        .catch { error ->
            val exception = when (error) {
                is HttpException -> {
                    when (error.code()) {
                        in 400..499 -> {
                            ClientException(
                                message = "${CLIENT_ERROR}: ${error.code()}",
                                cause = error,
                            )
                        }

                        in 500..599 -> {
                            ServerException(
                                message = "${SERVER_ERROR}: ${error.code()}",
                                cause = error
                            )
                        }

                        else -> {
                            UnknownException(
                                message = "${HTTP_UNKNOWN_ERROR}: ${error.code()}",
                                cause = error
                            )
                        }
                    }
                }

                is IOException -> NetworkException(
                    message = NETWORK_ERROR,
                    cause = error
                )

                else -> AppException(
                    message = UNKNOWN_ERROR,
                    cause = error
                )
            }

            val errorCode = when (error) {
                is HttpException -> {
                    when (error.code()) {
                        in 400..499 -> {
                            "#ER${error.code()}"
                        }

                        in 500..599 -> {
                            "#ER${error.code()}"
                        }

                        else -> {
                            "#ER${error.code()}"
                        }
                    }
                }

                else -> {
                    error.cause?.message.toString()
                }
            }
            emit(ResponseResource.Error(exception, errorCode))
        }
}

const val CLIENT_ERROR = "오류가 발생했습니다. 입력 내용을 확인해 주세요."
const val SERVER_ERROR = "서버에 오류가 발생했습니다. 나중에 다시 시도해 주세요."
const val NETWORK_ERROR = "인터넷 연결에 문제가 있습니다. 나중에 다시 시도해 주세요."
const val HTTP_UNKNOWN_ERROR = "알 수 없는 HTTP 오류 (예: 4xx/5xx)"
const val UNKNOWN_ERROR = "알 수 없는 오류가 발생했습니다."