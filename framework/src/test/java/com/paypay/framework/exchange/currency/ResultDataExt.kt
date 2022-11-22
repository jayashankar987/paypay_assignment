package com.paypay.framework.exchange.currency

import com.paypay.data.utils.ResultData
import com.paypay.data.utils.ResultData.Error
import com.paypay.data.utils.ResultData.Success
import com.paypay.data.utils.ResultError
import io.mockk.ConstantAnswer
import io.mockk.MockKStubScope
import org.junit.jupiter.api.Assertions

infix fun <T, E : ResultError, R : ResultData<T, E>, B> MockKStubScope<R, B>.returnsSuccess(returnValue: T) = answers(
    ConstantAnswer(Success<T, E>(returnValue) as R)
)

infix fun <T, E : ResultError, R : ResultData<T, E>, B> MockKStubScope<R, B>.returnsError(returnValue: E) = answers(
    ConstantAnswer(Error<T, E>(returnValue) as R)
)

infix fun <T, E : ResultError> ResultData<T, E>.shouldBeEqualToSuccess(expected: T) =
    this.apply { Assertions.assertEquals(Success<T, ResultError>(expected), this) }

infix fun <T, E : ResultError> ResultData<T, E>.shouldBeEqualToError(expected: E) =
    this.apply { Assertions.assertEquals(Error<T, ResultError>(e = expected), this) }