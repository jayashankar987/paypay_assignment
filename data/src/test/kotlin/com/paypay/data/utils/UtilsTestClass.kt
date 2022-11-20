package com.paypay.data.utils

import com.nhaarman.mockitokotlin2.any
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.Rule
import org.junit.jupiter.api.extension.ExtendWith
import retrofit2.Response
import kotlin.test.Test

@ExtendWith(MockKExtension::class)
internal class UtilsTestClass {

    @get:Rule
    val mockkRule = MockKRule(this)

    @RelaxedMockK
    lateinit var response: Response<*>

    @Test
    fun test_isUpdateRequired() {
        every { response.code() } returns any()
        Utils.isUpdateRequired(response)
        verify { response.code() }
        confirmVerified(response)
    }

    @Test
    fun test_isUpdateRequired_true() {
        every { response.code() } returns 304
        Utils.isUpdateRequired(response)
        verify { response.code() }
        confirmVerified(response)
    }
}