package com.paypay.currencyconverter.videomodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paypay.currencyconverter.util.Result
import com.paypay.currencyconverter.util.Result.Companion
import com.paypay.currencyconverter.util.Status.ERROR
import com.paypay.currencyconverter.util.Status.LOADING
import com.paypay.currencyconverter.util.Status.SUCCESS
import com.paypay.framework.exchange.currency.model.CurrencyData
import com.paypay.framework.exchange.currency.repository.converter.ICurrencyConverterRepository
import com.paypay.framework.exchange.currency.repository.currency.ICurrencyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class ExchangeCurrencyViewModel
@Inject constructor(private val currencyConverterRepo: ICurrencyConverterRepository) : ViewModel() {

    private var _uiStateFlow = MutableStateFlow<Result<List<CurrencyData>>>(Result.loading(null))
    val uiState = _uiStateFlow.asStateFlow()

    fun fetchCurrencyRates(base: String, value: Float) {
        viewModelScope.launch {
            if(_uiStateFlow.value.status == LOADING) {
                return@launch
            }
            _uiStateFlow.update { Result.loading(null) }
            currencyConverterRepo.getConversionForAllCurrencies(base = base, input = value).flowOn(Dispatchers.Default)
                .catch { error ->
                    _uiStateFlow.update { Result.error(msg = error.message.toString(), data = null) }
                }.collect { currencyList ->
                    _uiStateFlow.update { Result.success(currencyList) }
                }

            _uiStateFlow
        }
    }
}