package com.paypay.framework.exchange.currency.repository.currency

import android.content.SharedPreferences
import com.nhaarman.mockitokotlin2.any
import com.paypay.data.datasource.network.IExchangeNetworkSource
import com.paypay.data.model.CurrencyResponse
import com.paypay.data.utils.ResultData
import com.paypay.framework.exchange.currency.CoroutineTestExtension
import com.paypay.framework.exchange.currency.model.CurrencyData
import com.paypay.framework.exchange.currency.persistence.IExchangeLocalSource
import com.paypay.framework.exchange.currency.shouldBeEqualToSuccess
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@ExtendWith(CoroutineTestExtension::class)
@RunWith(MockitoJUnitRunner::class)
internal class CurrencyRepositoryTest {

    @MockK
    lateinit var exchangeLocalSource: IExchangeLocalSource

    @MockK
    lateinit var exchangeNetworkSource: IExchangeNetworkSource

    @MockK
    lateinit var sharedPreferences: SharedPreferences

    lateinit var currencyRepository: ICurrencyRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        currencyRepository = CurrencyRepository(
            exchangeNetworkSource = exchangeNetworkSource,
            exchangeLocalSource = exchangeLocalSource,
            sharedPreferences = sharedPreferences
        )

        sampleRates["XYZ"] = 9.04
        sampleRates["ABC"] = 3.04
        sampleRates["GHF"] = 1.0

        sampleCurrencies["XYZ"] = "xyz"
        sampleCurrencies["ABC"] = "abc"
        sampleCurrencies["GHF"] = "ghf"

    }

    @org.junit.Test
    internal fun `get currency Data fetch from remote isRefresh true`() = runTest {
        coEvery { exchangeLocalSource.getAllCurrenciesWithExchange() } returns emptyList()
        coEvery { exchangeNetworkSource.getCurrenciesExchangeRates("", "any()") } returns currencyResponse
        coEvery { exchangeNetworkSource.getCurrencies() } returns sampleCurrencies
        coEvery { exchangeLocalSource.saveCurrencyExchangeRates(exchangeData) } returns Unit

        currencyRepository.fetchCurrencyExchangeRates(true) shouldBeEqualToSuccess exchangeData

        coVerifyOrder {
            exchangeLocalSource.getAllCurrenciesWithExchange()
            exchangeNetworkSource.getCurrenciesExchangeRates(any(), any())
            exchangeNetworkSource.getCurrencies()
            exchangeLocalSource.saveCurrencyExchangeRates(exchangeData)
        }
        confirmVerified(exchangeNetworkSource, exchangeLocalSource)
    }

    @org.junit.Test
    fun `get currency Data from chache when failed from network`() = runTest {
        coEvery { exchangeLocalSource.getAllCurrenciesWithExchange() } returns exchangeData

        currencyRepository.fetchCurrencyExchangeRates(false) shouldBeEqualToSuccess exchangeData

        coVerify {
            exchangeLocalSource.getAllCurrenciesWithExchange()
        }
        coVerify(exactly = 0) { exchangeNetworkSource.getCurrenciesExchangeRates(any(), any()) }

        confirmVerified(exchangeLocalSource, exchangeNetworkSource)
    }

    private val sampleRates = mutableMapOf<String, Double>()
    private val sampleCurrencies = mutableMapOf<String, String>()
    private val currencyResponse: CurrencyResponse = CurrencyResponse(
        rates = sampleRates, base = any(), disclaimer = any(), license = any(), timestamp = any()
    )

    private val exchangeData = listOf(
        CurrencyData(currencyName = "xyz", currencyCode = "XYZ", currencyValue = 9.04),
        CurrencyData(currencyName = "abc", currencyCode = "ABC", currencyValue = 3.04),
        CurrencyData(currencyName = "ghf", currencyCode = "GHF", currencyValue = 1.0)
    )
}