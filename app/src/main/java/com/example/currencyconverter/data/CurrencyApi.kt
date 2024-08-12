package com.example.currencyconverter.data

import com.example.currencyconverter.data.models.ConversionRates
import com.example.currencyconverter.data.models.CurrencyResponse
import com.example.currencyconverter.utilities.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CurrencyApi {
    @GET("/v6/{apiKey}/latest/{base}")
    suspend fun getRates(
        @Path("base") baseCurrency: String,
        @Path("apiKey") apiKey: String = API_KEY,
    ): Response<CurrencyResponse>
}