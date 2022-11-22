package com.paypay.data.utils

import com.paypay.data.exception.AppException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

sealed class ResultData<T, E : ResultError> {
    data class Success<T, E : ResultError>(val value: T) : ResultData<T, E>()
    data class Error<T, E : ResultError>(val e: E) : ResultData<T, E>()
}

interface ResultError {
    val cause: Throwable?
}

open class DataFetchError(open val ex: AppException) : ResultError {
    override val cause: Throwable? get() = ex
}

suspend inline fun <R> runSuspendCatching(
    dispatcher: CoroutineDispatcher = Dispatchers.IO, crossinline block: suspend () -> R
): ResultData<R, DataFetchError> = withContext(dispatcher) {
    try {
        ResultData.Success(block())
    } catch (e: Exception) {
        return@withContext ResultData.Error(DataFetchError(AppException.mapper(e)))
    }
}

//extensions for success
inline fun <T, E : ResultError> ResultData<T, E>.onSuccess(success: (value: T) -> Unit): ResultData<T, E> =
    apply { getOrNull()?.let(success) }

//extensions for error
inline fun <T, E : ResultError> ResultData<T, E>.onError(error: (error: E) -> Unit): ResultData<T, E> =
    apply { errorOrNull()?.let(error) }

fun <V, E : ResultError> ResultData<V, E>.getOrNull(): V? = (this as? ResultData.Success)?.value
fun <V, E : ResultError> ResultData<V, E>.errorOrNull(): E? = (this as? ResultData.Error)?.e