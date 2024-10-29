package com.hong7.coinnews.data.extensions

import com.hong7.coinnews.model.exception.BadRequestException
import com.hong7.coinnews.model.exception.ConflictException
import com.hong7.coinnews.model.exception.ForbiddenException
import com.hong7.coinnews.model.exception.InternetServerException
import com.hong7.coinnews.model.exception.NetworkNotConnectedException
import com.hong7.coinnews.model.exception.NotFoundException
import com.hong7.coinnews.model.exception.ResponseResource
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
        .onStart { emit(ResponseResource.Loading) }
        .catch { error ->
            val exception = when (error) {
                is HttpException -> {
                    when (error.code()) {
                        400 -> BadRequestException(message = "BadRequestException: ${error.code()}", cause = error)
                        403 -> ForbiddenException(message = "ForbiddenException: ${error.code()}", cause = error)
                        404 -> NotFoundException(message = "NotFoundException: ${error.code()}", cause = error)
                        409 -> ConflictException(message = "ConflictException: ${error.code()}", cause = error)
                        in 500..599 -> InternetServerException(
                            message = error.message(),
                            cause = error
                        )
                        else -> UnknownException(
                            message = "UnknownException: ${error.code()}",
                            cause = error
                        )
                    }
                }

                is IOException -> NetworkNotConnectedException(
                    message = "NetworkNotConnectedException",
                    cause = error
                )

                else -> UnknownException(
                    message = "UnknownException",
                    cause = error
                )
            }
            emit(ResponseResource.Error(exception))
        }
}

