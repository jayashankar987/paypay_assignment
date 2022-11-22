package com.paypay.data.datasource.network

import com.paypay.data.datasource.CoroutineTestExtension
import com.paypay.data.datasource.network.service.ExchangeService
import com.paypay.data.model.CurrencyResponse
import com.paypay.data.utils.ResultData.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.core.ValueClassSupport.boxedValue
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.net.UnknownHostException
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(CoroutineTestExtension::class)
class ExchangeNetworkSourceTest {

    @MockK
    lateinit var exchangeService: ExchangeService

    private lateinit var exchangeNetworkSource: IExchangeNetworkSource
    private lateinit var mockExchangeNetworkSource: IExchangeNetworkSource

    private val exchangeData = mutableMapOf<String, String>()

    @BeforeEach
    fun setup() {
        MockKAnnotations.init(this)
        exchangeData["USD"] = "United States of America"
        exchangeData["INR"] = "India"
        exchangeData["XYZ"] = "XYZ"
        mockExchangeNetworkSource = ExchangeNetworkSource(exchangeService)
    }

    @Test
    fun getCurrenciesExchangeRatesSuccess() {
        val api = Retrofit.Builder().baseUrl("https://openexchangerates.org/api/")
            .addConverterFactory(MoshiConverterFactory.create().asLenient()).build().create(ExchangeService::class.java)
        exchangeNetworkSource = ExchangeNetworkSource(api)
        runBlocking {
            val actual = exchangeNetworkSource.getCurrenciesExchangeRates("569da2e374854354afd5b1187144c67a", "USD")
            assertTrue(
                actual.boxedValue is CurrencyResponse,
                message = "expected a Success response based on base_url, valid_app_id, base (currently USD)"
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
                actual.boxedValue is Map<*, *>,
                message = "Currencies is open API should return success even without app_id and base"
            )
        }
    }

    @Test
    fun `basix mock test if map of currencies is returned`() = runTest {
        coEvery {
            exchangeService.getCurrencies()
        } returns exchangeData

        val actual = mockExchangeNetworkSource.getCurrencies()
        assert(exchangeData == actual)
        coVerify {
            exchangeService.getCurrencies()
        }
        confirmVerified(exchangeService)
    }

    @Test
    fun `verify of returning empty currencies`() = runTest {
        coEvery {
            exchangeService.getCurrencies()
        } returns emptyMap()
        val actual = mockExchangeNetworkSource.getCurrencies()
        assert(actual.isEmpty())
        coVerify {
            exchangeService.getCurrencies()
        }
        confirmVerified(exchangeService)
    }

    @After
    fun tearDown() {
    }
}
