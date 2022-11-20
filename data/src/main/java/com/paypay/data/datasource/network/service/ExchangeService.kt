package com.paypay.data.datasource.network.service

import com.paypay.data.model.CurrencyResponse
import com.paypay.data.utils.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @author jayashankar
 */
interface ExchangeService {

    /**
     * @author jayashankar
     * API to fetch all currencies with name and show the respective currency to be showcased to users
     *
     * @return a network response with list of currencies in json when 200
     */
    @GET(Constants.CURRENCIES)
    suspend fun getCurrencies(): Response<Map<String, String>>

    /**
     * @author jayashankar
     * @param appId token which is registered in https://openexchangerates.org
     * @param base base exchange currency to be able to convert
     *
     * here base is by default targeted to USD as the free account supports only USD as base,
     * in case of premium or developer account base can be changed to different currency
     *
     * @return network response with currency response with all the conversions w.r.t base when 200
     */
    @GET(Constants.LATEST_DATA)
    suspend fun getLatestRates(
        @Query(Constants.PARAM_APP_ID) appId: String,
        @Query(Constants.PARAM_BASE) base: String? = "USD"
    ): Response<CurrencyResponse>
}