package com.paypay.data.datasource.network

import com.paypay.data.datasource.network.service.ExchangeService
import com.paypay.data.utils.ResultData.Error
import com.paypay.data.utils.ResultData.Success
import io.mockk.MockKAnnotations
import io.mockk.core.ValueClassSupport.boxedValue
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import kotlin.test.assertTrue

@ExtendWith(MockKExtension::class)
internal class ExchangeNetworkSourceTest {

    private lateinit var exchangeNetworkSource: IExchangeNetworkSource

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxed = true)

    }

    @Test
    fun getCurrenciesExchangeRatesSuccess() {
        val api = Retrofit.Builder().baseUrl("https://openexchangerates.org/api/")
            .addConverterFactory(MoshiConverterFactory.create().asLenient()).build().create(ExchangeService::class.java)
        exchangeNetworkSource = ExchangeNetworkSource(api)
        runBlocking {
            val actual = exchangeNetworkSource.getCurrenciesExchangeRates("569da2e374854354afd5b1187144c67a", "USD")
            assertTrue(
                actual.boxedValue is Success<*>,
                message = "expected a Success response based on base_url, valid_app_id, base (currently USD)"
            )
        }
    }

    @Test
    fun getCurrenciesExchangeRatesError() {
        val api = Retrofit.Builder().baseUrl("https://openexchangerates.or/api/")
            .addConverterFactory(MoshiConverterFactory.create().asLenient()).build().create(ExchangeService::class.java)
        exchangeNetworkSource = ExchangeNetworkSource(api)
        runBlocking {
            val actual = exchangeNetworkSource.getCurrenciesExchangeRates("569da2e374854354afd5", "")
            assertTrue(
                actual.boxedValue is Error<*>,
                message = "Expected to be a Error Result with wrong url ,app_id, base inputs"
            )
        }
    }

    @Test
    fun getCurrencies() {
        val api = Retrofit.Builder().baseUrl("https://openexchangerates.org/api/")
            .addConverterFactory(MoshiConverterFactory.create().asLenient()).build().create(ExchangeService::class.java)
        exchangeNetworkSource = ExchangeNetworkSource(api)
        runBlocking {
            val actual = exchangeNetworkSource.getCurrencies()
            assertTrue(
                actual.boxedValue is Success<*>,
                message = "Currencies is open API should return success even without app_id and base"
            )
        }
    }

    @After
    fun tearDown() {
    }
}