package com.paypay.data.datasource.network

sealed class NetworkResponse<out T> {
    data class Success<T>(val value: T) : NetworkResponse<T>()
    data class Error<T>(val value: T?) : NetworkResponse<T>()
    object NoUpdateRequired : NetworkResponse<Nothing>()
}