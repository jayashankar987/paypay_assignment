package com.paypay.currencyconverter.videomodel

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paypay.currencyconverter.videomodel.CurrenciesFetchState.Error
import com.paypay.currencyconverter.videomodel.CurrenciesFetchState.Loading
import com.paypay.currencyconverter.videomodel.CurrenciesFetchState.Success
import com.paypay.framework.exchange.currency.model.CurrencyData
import com.paypay.framework.exchange.currency.repository.converter.ICurrencyConverterUsecase
import com.paypay.framework.exchange.currency.utils.FrameworkConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class ExchangeCurrencyViewModel
@Inject constructor(
    private val currencyConverterUsecase: ICurrencyConverterUsecase, private val sharedPreferences: SharedPreferences
) : ViewModel() {

    init {
        fetchCurrencies()
    }

    private var _uiStateFlow: MutableStateFlow<CurrenciesFetchState>? = MutableStateFlow(Success(data = emptyList()))
    val uiState: StateFlow<CurrenciesFetchState> = _uiStateFlow!!

    private var _currenciesStateFlow: MutableStateFlow<CurrenciesFetchState>? = MutableStateFlow(Success(data = emptyList()))

    private var previousBase: String = ""

    fun fetchCurrencyRates(base: String, input: Double) {
        viewModelScope.launch {
            _uiStateFlow?.value = Loading
            val forceRefresh = isApiFetchNeeded()
            flow {
                if (!forceRefresh && _currenciesStateFlow?.value is Success && previousBase == base) {
                    emit((_currenciesStateFlow?.value as Success).data)
                } else {
                    previousBase = base
                    emit(currencyConverterUsecase.getConversionForAllCurrencies(base, forceRefresh))
                }
            }.catch { exception ->
                _currenciesStateFlow?.value = Error(throwable = exception)
                _uiStateFlow?.value = Error(throwable = exception)
            }.cancellable().collect { list ->
                if (forceRefresh) {
                    _currenciesStateFlow?.value = Success(data = list)
                }
                val result = mutableListOf<CurrencyData>()
                list.forEach {
                    result.add(
                        CurrencyData(
                            currencyCode = it.currencyCode,
                            currencyValue = (it.currencyValue ?: 1.0) * input,
                            currencyName = it.currencyName
                        )
                    )
                }
                _uiStateFlow?.value = Success(data = result)

            }
        }
    }

    private fun fetchCurrencies(base: String = "USD") {
        viewModelScope.launch {
            _uiStateFlow?.value = Loading
            flow {
                val forceRefresh = isApiFetchNeeded()
                emit(currencyConverterUsecase.getConversionForAllCurrencies(base, forceRefresh))

            }.catch { throwable ->
                _currenciesStateFlow?.value = Error(throwable = throwable)
                _uiStateFlow?.value = Error(throwable = throwable)
            }.cancellable().collect { list ->
                val currencies = mutableListOf<Pair<String, String>>()
                list.forEach {
                    currencies.add(Pair(it.currencyCode, it.currencyName))
                }
                _currenciesStateFlow?.value = Success(data = list)
                _uiStateFlow?.value = CurrenciesFetchState.Init(data = currencies)
            }
        }
    }

    private suspend fun isApiFetchNeeded(): Boolean = withContext(Dispatchers.IO) {
        val threshold = TimeUnit.MINUTES.toMillis(0)
        val lastSyncedTime = sharedPreferences.getLong(FrameworkConstants.PREF_LAST_SYNC_TIMESTAMP, 0L)
        return@withContext (System.currentTimeMillis() - lastSyncedTime) > threshold
    }
}

sealed class CurrenciesFetchState {
    data class Init(val data: List<Pair<String, String>>? = null) : CurrenciesFetchState()
    data class Error(val throwable: Throwable) : CurrenciesFetchState()
    data class Success(val data: List<CurrencyData>) : CurrenciesFetchState()
    object Loading : CurrenciesFetchState()
}
