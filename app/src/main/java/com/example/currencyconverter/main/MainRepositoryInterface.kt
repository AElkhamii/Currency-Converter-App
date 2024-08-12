package com.example.currencyconverter.main

import com.example.currencyconverter.data.models.CurrencyResponse
import com.example.currencyconverter.utilities.Resource
import retrofit2.Response

interface MainRepositoryInterface {
    suspend fun getRates(base:String): Resource<CurrencyResponse>
}