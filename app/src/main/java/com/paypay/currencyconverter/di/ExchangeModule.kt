package com.paypay.currencyconverter.di

import android.content.SharedPreferences
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.viewModelFactory
import com.paypay.currencyconverter.videomodel.ExchangeCurrencyViewModel
import com.paypay.framework.exchange.currency.repository.converter.ICurrencyConverterUsecase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent

@Module
@InstallIn(FragmentComponent::class)
class ExchangeModule {

    @Provides
    fun provideExchangeModule(
        currencyConverterUsecase: ICurrencyConverterUsecase, sharedPreferences: SharedPreferences
    ): ViewModelProvider.Factory {
        return viewModelFactory {
            ExchangeCurrencyViewModel(
                currencyConverterUsecase = currencyConverterUsecase, sharedPreferences = sharedPreferences
            )
        }
    }
}