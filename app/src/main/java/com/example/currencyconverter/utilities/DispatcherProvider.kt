package com.example.currencyconverter.utilities

import kotlinx.coroutines.CoroutineDispatcher


/* this class used for testing view model,
 * so for the viewmodel we have in our test cases
 * we can pass the test dispatcher for all of those */
interface DispatcherProvider {
    val main: CoroutineDispatcher
    val io: CoroutineDispatcher
    val default: CoroutineDispatcher
    val unconfined: CoroutineDispatcher
}

/* This help us with our real app we can pass:
 * main dispatcher for the main dispatcher
 * io dispatcher for the io dispatcher ...etc */