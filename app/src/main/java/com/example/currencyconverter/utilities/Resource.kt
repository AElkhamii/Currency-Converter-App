package com.example.currencyconverter.utilities

/* This generic class used to handel errors
 * because we cannot handel errors in the view model, we will use this class to handel this problem */
sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    /* This classes is responsible for handling the state of the api request */
    class Success<T>(data: T): Resource<T>(data, null)
    class Error<T>(message:String): Resource<T>(null,message)
//    class Loading<T>():Resource<T>(null,null)
}

/* All of these states are known at compile time, so we can use when statement on it to determine the state of response */