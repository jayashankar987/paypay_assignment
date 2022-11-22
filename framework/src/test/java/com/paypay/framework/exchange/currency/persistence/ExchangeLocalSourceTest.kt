package com.paypay.framework.exchange.currency.persistence

import com.nhaarman.mockitokotlin2.any
import com.paypay.framework.exchange.currency.model.CurrencyData
import com.paypay.framework.exchange.currency.persistence.dao.IExchangeDao
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ExchangeLocalSourceTest {

    @MockK
    lateinit var exchangeDao: IExchangeDao

    private lateinit var exchangeLocalSource: IExchangeLocalSource
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
        coEvery { exchangeDao.insertCurrencyExchangeRates(any()) } returns Unit

        exchangeLocalSource.saveCurrencyExchangeRates(any())
        verify { exchangeDao.insertCurrencyExchangeRates(any()) }
        confirmVerified(exchangeData)
    }

    @Test
    fun getAllCurrenciesWithExchange() = runTest {
        coEvery {
            exchangeDao.getAllCurrencyExchangeRates()
        } returns exchangeData

        exchangeLocalSource.getAllCurrenciesWithExchange()
        verify {
            exchangeDao.getAllCurrencyExchangeRates()
        }
        confirmVerified(exchangeDao)
    }

    @Test
    fun `verify if empty list is return`() = runTest {
        coEvery { exchangeDao.getAllCurrencyExchangeRates() } returns emptyList()
        val actual = exchangeLocalSource.getAllCurrenciesWithExchange()
        assert(actual.isEmpty())
        verify { exchangeDao.getAllCurrencyExchangeRates() }
        confirmVerified(exchangeDao)
    }
}