package com.paypay.framework.exchange.currency.persistence

import com.paypay.framework.exchange.currency.CoroutineTestExtension
import com.paypay.framework.exchange.currency.model.CurrencyData
import com.paypay.framework.exchange.currency.persistence.dao.IExchangeDao
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(CoroutineTestExtension::class)
class ExchangeLocalSourceTest {

    @MockK
    lateinit var exchangeDao: IExchangeDao

    lateinit var exchangeLocalSource: IExchangeLocalSource
    private val exchangeData = listOf(
        CurrencyData(currencyName = "xyz", currencyCode = "XYZ", currencyValue = 9.04),
        CurrencyData(currencyName = "abc", currencyCode = "ABC", currencyValue = 3.04),
        CurrencyData(currencyName = "ghf", currencyCode = "GHF", currencyValue = 1.0)
    )

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        exchangeLocalSource = ExchangeLocalSource(exchangeDao)
    }

    @Test
    fun saveCurrencyExchangeRates() = runTest {
        coEvery {
            exchangeDao.insertCurrencyExchangeRates(any())
        } returns Unit
        runBlocking {
            exchangeLocalSource.saveCurrencyExchangeRates(exchangeData)
        }
        verify {
            exchangeDao.insertCurrencyExchangeRates(any())
        }
        confirmVerified(exchangeDao)
    }

    @Test
    fun getAllCurrenciesWithExchange() {
    }
}