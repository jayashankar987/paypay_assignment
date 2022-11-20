package com.paypay.currencyconverter.util

import java.text.DecimalFormat

object Utils {
    private val normalFormatter by lazy { DecimalFormat("#.##") }

    fun getDecimalDataForDouble(input: Double): String {
        if (input > 100000000) {
            return String.format("%.9E",input)
        }
        return normalFormatter.format(input)
    }
}