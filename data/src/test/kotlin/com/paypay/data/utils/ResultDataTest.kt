package com.paypay.data.utils

import com.paypay.data.datasource.CoroutineTestExtension
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(CoroutineTestExtension::class)
internal class ResultDataTest {

    @Test
    internal fun `runSuspendCatching success validation`() = runTest {
        val result = runSuspendCatching {
            delay(500)
        }
        assert(result is ResultData.Success)
    }

    @Test
    internal fun `runSuspendCatching error validation`() = runTest {
        val error = Exception("error case")
        val result = runSuspendCatching {
            delay(500)
            throw error
        }
        with(result as? ResultData.Error) {
            assert(this != null)
            assert(this?.e?.ex?.message == error.message)
        }
    }
}