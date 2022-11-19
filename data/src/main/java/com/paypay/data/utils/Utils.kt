package com.paypay.data.utils

import retrofit2.Response

object Utils {

    fun isUpdateRequired(response: Response<*>): Boolean {
        response.code().let {
            return it != 304
        }
    }
}