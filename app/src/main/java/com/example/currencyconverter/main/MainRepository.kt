package com.example.currencyconverter.main

import android.util.Log
import com.example.currencyconverter.data.CurrencyApi
import com.example.currencyconverter.data.models.CurrencyResponse
import com.example.currencyconverter.utilities.Resource
import retrofit2.Response
import javax.inject.Inject

class MainRepository @Inject constructor (private val api: CurrencyApi): MainRepositoryInterface {

    /* we will handel the Response<Currency Response> in this function because we could not handel it in the viewmodel,
     * and according to the state result this function will return the Resource state with the data  */
    override suspend fun getRates(base:String):Resource<CurrencyResponse>{
         try {
            // Get the response
            val response = api.getRates(base)

            //check if the response is done successfully
            if(response.isSuccessful){
                /* Return resource instance with the body of the response */
                 response.body()?.let {
                     return Resource.Success(it)

                }
            }
            /* In the other case Return resource instance with the error message of the response */
             return Resource.Error(response.message() ?: "Request Failed")
        }catch (e: Exception){
             return Resource.Error(e.message ?: "Error occurred")
        }
    }
}