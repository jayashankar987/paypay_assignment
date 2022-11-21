package com.paypay.data.datasource.network

import com.nhaarman.mockitokotlin2.any
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import io.mockk.junit5.MockKExtension
import org.junit.Rule
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class DataRetrofitClientTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @RelaxedMockK
    lateinit var dataRetrofitClient: DataRetrofitClient

    @Test
    fun getDefaultRetrofit() {
        every { dataRetrofitClient.getDefaultRetrofit(any()) } returns any()
        dataRetrofitClient.getDefaultRetrofit(any())
        io.mockk.verify(exactly = 1) { dataRetrofitClient.getDefaultRetrofit(any()) }
        confirmVerified(dataRetrofitClient)
    }
}