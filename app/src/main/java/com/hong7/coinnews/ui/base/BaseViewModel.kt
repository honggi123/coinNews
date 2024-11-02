package com.hong7.coinnews.ui.base

import androidx.lifecycle.ViewModel
import com.hong7.coinnews.model.exception.AppException
import com.hong7.coinnews.model.exception.BadRequestException
import com.hong7.coinnews.model.exception.ConflictException
import com.hong7.coinnews.model.exception.ForbiddenException
import com.hong7.coinnews.model.exception.InternetServerException
import com.hong7.coinnews.model.exception.NetworkNotConnectedException
import com.hong7.coinnews.model.exception.NotFoundException
import com.hong7.coinnews.model.exception.ResponseResource
import com.hong7.coinnews.model.exception.UnknownException
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import timber.log.Timber

open class BaseViewModel : ViewModel() {


    private val _messageEvent = MutableSharedFlow<String>()
    val messageEvent = _messageEvent.asSharedFlow()

    suspend fun onErrorResonse(result: ResponseResource.Error) {
        val message = when (result.exception) {
            is BadRequestException -> "잘못된 요청입니다. 다시 시도해주세요."
            is NotFoundException -> "요청한 정보를 찾을 수 없습니다."
            is ConflictException -> "충돌이 발생했습니다. 다시 시도해주세요."
            is InternetServerException -> "서버에 문제가 발생했습니다. 잠시 후 다시 시도해주세요."
            is ForbiddenException -> "접근 권한이 없습니다."
            is NetworkNotConnectedException -> "인터넷 연결이 필요합니다. 연결 상태를 확인해주세요."
            else -> {
                Timber.tag("uncaughtNetworkException").e(result.exception)
                throw result.exception
            }
        }
        _messageEvent.emit(message)
    }
}

