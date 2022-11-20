package com.paypay.currencyconverter.videomodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paypay.currencyconverter.videomodel.CurrenciesFetchState.Loading
import com.paypay.currencyconverter.videomodel.CurrenciesFetchState.Success
import com.paypay.framework.exchange.currency.model.CurrencyData
import com.paypay.framework.exchange.currency.repository.converter.ICurrencyConverterUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class ExchangeCurrencyViewModel
@Inject constructor(private val currencyConverterUsecase: ICurrencyConverterUsecase) :
    ViewModel() {

    private var _uiStateFlow = MutableStateFlow<CurrenciesFetchState>(Success(data = emptyList()))
    val uiState: StateFlow<CurrenciesFetchState> = _uiStateFlow

    private var lastRequestedTime = 0L
    private val thresholdMinutes = 5L
    private val time: TimeUnit = TimeUnit.MINUTES

    fun fetchCurrencyRates(base: String, input: Double) {
        viewModelScope.launch {
            _uiStateFlow.value = Loading
            flow {
                if (needsFetchingFromRepo()) {
                    emit(currencyConverterUsecase.getConversionForAllCurrencies(base))
                } else {
                    emit(currencyConverterUsecase.fetchCachedCurrencyDetails(base))
                }

            }.catch { exception ->
                _uiStateFlow.value = CurrenciesFetchState.Error(throwable = exception)
            }.cancellable().collect { list ->
                lastRequestedTime = System.currentTimeMillis()
                list.forEach {
                    it.currencyValue = input * it.currencyValue!!
                }
                _uiStateFlow.value = Success(data = list)
            }
        }
    }

    fun fetchCurrencies(base: String = "USD") {
        viewModelScope.launch {
            _uiStateFlow.value = Loading
            flow {
                if (needsFetchingFromRepo()) {
                    emit(currencyConverterUsecase.getConversionForAllCurrencies(base))
                } else {
                    emit(currencyConverterUsecase.fetchCachedCurrencyDetails(base))
                }

            }.catch { throwable ->
                _uiStateFlow.value = CurrenciesFetchState.Error(throwable = throwable)
            }.cancellable().collect { list ->
                val currencies = mutableListOf<Pair<String, String>>()
                list.forEach {
                    currencies.add(Pair(it.currencyCode, it.currencyName))
                }
                _uiStateFlow.value = CurrenciesFetchState.Init(data = currencies)
            }
        }
    }

    private fun needsFetchingFromRepo(): Boolean {
        val currentRequestTime = System.currentTimeMillis()
        if (currentRequestTime - lastRequestedTime > time.convert(thresholdMinutes, TimeUnit.MILLISECONDS)) {
            return true
        }
        return false
    }
}

sealed class CurrenciesFetchState {
    data class Init(val data: List<Pair<String, String>>? = null) : CurrenciesFetchState()
    data class Error(val throwable: Throwable) : CurrenciesFetchState()
    data class Success(val data: List<CurrencyData>) : CurrenciesFetchState()
    object Loading : CurrenciesFetchState()
}
