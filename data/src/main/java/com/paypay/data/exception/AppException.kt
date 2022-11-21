package com.paypay.data.exception

import java.net.SocketTimeoutException
import java.net.UnknownHostException

sealed class AppException : Throwable() {

    data class Offline(
        override val cause: Throwable? = null, override val message: String? = null
    ) : AppException()

    data class ExchangeException(
        override val message: String? = null, override val cause: Throwable? = null
    ) : AppException()

    companion object {
        val mapper: (Throwable) -> AppException = {
            when (it) {
                is AppException -> it
                is SocketTimeoutException, is UnknownHostException -> Offline(cause = it.cause, message = it.message)
                else -> ExchangeException(cause = it.cause, message = it.message)
            }
        }
    }
}