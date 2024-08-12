package com.example.currencyconverter.di

import com.example.currencyconverter.data.CurrencyApi
import com.example.currencyconverter.main.MainRepository
import com.example.currencyconverter.main.MainRepositoryInterface
import com.example.currencyconverter.utilities.Constants.Companion.BASE_URL
import com.example.currencyconverter.utilities.DispatcherProvider
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun providerCurrencyApi(): CurrencyApi {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val clint = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(clint)
            .build()
            .create(CurrencyApi::class.java)
    }

    @Singleton
    @Provides
    fun providerMainRepository(api: CurrencyApi): MainRepositoryInterface{
        return MainRepository(api)
    }

    @Singleton
    @Provides
    fun providerDispatcher(): DispatcherProvider{
        return object: DispatcherProvider{
            override val main: CoroutineDispatcher
                get() = Dispatchers.Main
            override val io: CoroutineDispatcher
                get() = Dispatchers.IO
            override val default: CoroutineDispatcher
                get() = Dispatchers.Default
            override val unconfined: CoroutineDispatcher
                get() = Dispatchers.Unconfined
        }
    }
}