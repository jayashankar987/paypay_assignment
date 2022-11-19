package com.paypay.framework.exchange.currency.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.paypay.data.datasource.network.DataRetrofitClient

class CurrencyExchangeRetrofitClient(private val context: Context) : DataRetrofitClient() {

    override fun hasNetwork(): Boolean {
        var isConnected = false
        context.let {
            val cm = it.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
            cm?.run {
                getNetworkCapabilities(activeNetwork)?.run {
                    isConnected = when {
                        hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                        else -> false
                    }
                }
            }
            //Timber.d("Is Network Connected = $isConnected")
        }
        return isConnected
    }
}