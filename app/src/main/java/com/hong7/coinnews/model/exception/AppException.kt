package com.hong7.coinnews.model.exception

sealed interface ResponseResource<out T> {
    data class Success<T>(val data: T) : ResponseResource<T>
    data class Error(val exception: AppException, val errorCode: String? = null) :
        ResponseResource<Nothing>

    data class Loading(val status: Boolean) : ResponseResource<Nothing>
}

open class AppException(message: String? = null, cause: Throwable? = null) :
    Throwable(message, cause)

class BadRequestException(message: String? = null, cause: Throwable? = null) :
    AppException(message, cause)

class NotFoundException(message: String? = null, cause: Throwable? = null) :
    AppException(message, cause)

class ConflictException(message: String? = null, cause: Throwable? = null) :
    AppException(message, cause)

class InternetServerException(message: String? = null, cause: Throwable? = null) :
    AppException(message, cause)

class ForbiddenException(message: String? = null, cause: Throwable? = null) :
    AppException(message, cause)

class NetworkNotConnectedException(message: String? = null, cause: Throwable? = null) :
    AppException(message, cause)

class UnknownException(message: String? = null, cause: Throwable? = null) :
    AppException(message, cause)