package com.paypay.data.utils

sealed class ResultData<out T, e : Exception> {
    data class Success<T>(val value: T) : ResultData<T, Nothing>()
    data class Error<e : Exception>(val ex: e) : ResultData<Nothing, e>()
    object NoUpdateRequired : ResultData<Nothing, Nothing>()
}