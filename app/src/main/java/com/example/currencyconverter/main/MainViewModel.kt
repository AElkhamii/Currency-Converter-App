package com.example.currencyconverter.main

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.currencyconverter.utilities.DispatcherProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.example.currencyconverter.data.models.ConversionRates
import com.example.currencyconverter.utilities.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlin.math.round

/* We set dispatchers here in the constructor to make the viewmodel testable */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepositoryInterface,
    private val dispatchers: DispatcherProvider
):ViewModel() {
    /****** Events ******/
    sealed class CurrencyEvent{
        class Success(val resultText: String): CurrencyEvent()
        class Failure(val errorText: String): CurrencyEvent()

        object Loading: CurrencyEvent()
        object Empty: CurrencyEvent()
    }


    /****** Flow ******/
    private val _conversion = MutableStateFlow<CurrencyEvent>(CurrencyEvent.Empty)
    val conversion: StateFlow<CurrencyEvent> = _conversion

    fun convert(
        amountStr: String,  // Amount of money we want to convert
        fromCurrency: String,
        toCurrency: String
    ){
        val fromAmount = amountStr.toFloatOrNull()
        if(fromAmount == null){
            _conversion.value = CurrencyEvent.Failure("Not a valid amount")
            return
        }

        viewModelScope.launch(dispatchers.io) {

            _conversion.value = CurrencyEvent.Loading

            // Get the Response request from repository
            val ratesResponse = mainRepository.getRates(fromCurrency)
            Log.e("help","After request ${ratesResponse.data}")

            // check on the state of the response
            when(ratesResponse){
                is Resource.Error -> _conversion.value = CurrencyEvent.Failure(ratesResponse.message!!)
                is Resource.Success ->{
                    val rates = ratesResponse.data!!.conversion_rates
                    // actual rate
                    val rate = getRateFromCurrency(toCurrency,rates)
                    if(rate == null){
                        _conversion.value = CurrencyEvent.Failure("Unexpected error")
                    }
                    else{
                        // Result
                        val convertedCurrency = round(fromAmount * rate * 100) / 100
                        _conversion.value = CurrencyEvent.Success(
                            "$fromAmount $fromCurrency = $convertedCurrency $toCurrency"
                        )
                    }
                }
            }
        }
    }

    private fun getRateFromCurrency(currency: String, rates: ConversionRates): Double? = when(currency){
        "AED"-> rates.AED
        "AFN"-> rates.AFN
        "ALL"-> rates.ALL
        "AMD"-> rates.AMD
        "ANG"-> rates.ANG
        "AOA"-> rates.AOA
        "ARS"-> rates.ARS
        "AUD"-> rates.AUD
        "AWG"-> rates.AWG
        "AZN"-> rates.AZN
        "BAM"-> rates.BAM
        "BBD"-> rates.BBD
        "BDT"-> rates.BDT
        "BGN"-> rates.BGN
        "BHD"-> rates.BHD
        "BIF"-> rates.BIF
        "BMD"-> rates.BMD
        "BND"-> rates.BND
        "BOB"-> rates.BOB
        "BRL"-> rates.BRL
        "BSD"-> rates.BSD
        "BTN"-> rates.BTN
        "BWP"-> rates.BWP
        "BYN"-> rates.BYN
        "BZD"-> rates.BZD
        "CAD"-> rates.CAD
        "CDF"-> rates.CDF
        "CHF"-> rates.CHF
//        "CLP"-> rates.CLP
//        "CNY"-> rates.CNY
//        "COP"-> rates.COP
//        "CRC"-> rates.CRC
//        "CUP"-> rates.CUP
//        "CVE"-> rates.CVE
//        "CZK"-> rates.CZK
//        "DJF"-> rates.DJF
//        "DKK"-> rates.DKK
//        "DOP"-> rates.DOP
//        "DZD"-> rates.DZD
//        "EGP"-> rates.EGP
//        "ERN"-> rates.ERN
//        "ETB"-> rates.ETB
//        "EUR"-> rates.EUR
//        "FJD"-> rates.FJD
//        "FKP"-> rates.FKP
//        "FOK"-> rates.FOK
//        "GBP"-> rates.GBP
//        "GEL"-> rates.GEL
//        "GGP"-> rates.GGP
//        "GHS"-> rates.GHS
//        "GIP"-> rates.GIP
//        "GMD"-> rates.GMD
//        "GNF"-> rates.GNF
//        "GTQ"-> rates.GTQ
//        "GYD"-> rates.GYD
//        "HKD"-> rates.HKD
//        "HNL"-> rates.HNL
//        "HRK"-> rates.HRK
//        "HTG"-> rates.HTG
//        "HUF"-> rates.HUF
//        "IDR"-> rates.IDR
//        "ILS"-> rates.ILS
//        "IMP"-> rates.IMP
//        "INR"-> rates.INR
//        "IQD"-> rates.IQD
//        "IRR"-> rates.IRR
//        "ISK"-> rates.ISK
//        "JEP"-> rates.JEP
//        "JMD"-> rates.JMD
//        "JOD"-> rates.JOD
//        "JPY"-> rates.JPY
//        "KES"-> rates.KES
//        "KGS"-> rates.KGS
//        "KHR"-> rates.KHR
//        "KID"-> rates.KID
//        "KMF"-> rates.KMF
//        "KRW"-> rates.KRW
//        "KWD"-> rates.KWD
//        "KYD"-> rates.KYD
//        "KZT"-> rates.KZT
//        "LAK"-> rates.LAK
//        "LBP"-> rates.LBP
//        "LKR"-> rates.LKR
//        "LRD"-> rates.LRD
//        "LSL"-> rates.LSL
//        "LYD"-> rates.LYD
//        "MAD"-> rates.MAD
//        "MDL"-> rates.MDL
//        "MGA"-> rates.MGA
//        "MKD"-> rates.MKD
//        "MMK"-> rates.MMK
//        "MNT"-> rates.MNT
//        "MOP"-> rates.MOP
//        "MRU"-> rates.MRU
//        "MUR"-> rates.MUR
//        "MVR"-> rates.MVR
//        "MWK"-> rates.MWK
//        "MXN"-> rates.MXN
//        "MYR"-> rates.MYR
//        "MZN"-> rates.MZN
//        "NAD"-> rates.NAD
//        "NGN"-> rates.NGN
//        "NIO"-> rates.NIO
//        "NOK"-> rates.NOK
//        "NPR"-> rates.NPR
//        "NZD"-> rates.NZD
//        "OMR"-> rates.OMR
//        "PAB"-> rates.PAB
//        "PEN"-> rates.PEN
//        "PGK"-> rates.PGK
//        "PHP"-> rates.PHP
//        "PKR"-> rates.PKR
//        "PLN"-> rates.PLN
//        "PYG"-> rates.PYG
//        "QAR"-> rates.QAR
//        "RON"-> rates.RON
//        "RSD"-> rates.RSD
//        "RUB"-> rates.RUB
//        "RWF"-> rates.RWF
//        "SAR"-> rates.SAR
//        "SBD"-> rates.SBD
//        "SCR"-> rates.SCR
//        "SDG"-> rates.SDG
//        "SEK"-> rates.SEK
//        "SGD"-> rates.SGD
//        "SHP"-> rates.SHP
//        "SLE"-> rates.SLE
//        "SLL"-> rates.SLL
//        "SOS"-> rates.SOS
//        "SRD"-> rates.SRD
//        "SSP"-> rates.SSP
//        "STN"-> rates.STN
//        "SYP"-> rates.SYP
//        "SZL"-> rates.SZL
//        "THB"-> rates.THB
//        "TJS"-> rates.TJS
//        "TMT"-> rates.TMT
//        "TND"-> rates.TND
//        "TOP"-> rates.TOP
//        "TRY"-> rates.TRY
//        "TTD"-> rates.TTD
//        "TVD"-> rates.TVD
//        "TWD"-> rates.TWD
//        "TZS"-> rates.TZS
//        "UAH"-> rates.UAH
//        "UGX"-> rates.UGX
//        "USD"-> rates.USD
//        "UYU"-> rates.UYU
//        "UZS"-> rates.UZS
//        "VES"-> rates.VES
//        "VND"-> rates.VND
//        "VUV"-> rates.VUV
//        "WST"-> rates.WST
//        "XAF"-> rates.XAF
//        "XCD"-> rates.XCD
//        "XDR"-> rates.XDR
//        "XOF"-> rates.XOF
//        "XPF"-> rates.XPF
//        "YER"-> rates.YER
//        "ZAR"-> rates.ZAR
//        "ZMW"-> rates.ZMW
//        "ZWL"-> rates.ZWL
        else -> null
    }
}

//private fun getRateForCurrency(currency: String, rates: Rates) = when (currency) {
//    "CAD" -> rates.cAD
//    "HKD" -> rates.hKD
//    "ISK" -> rates.iSK
//    "EUR" -> rates.eUR
//    "PHP" -> rates.pHP
//    "DKK" -> rates.dKK
//    "HUF" -> rates.hUF
//    "CZK" -> rates.cZK
//    "AUD" -> rates.aUD
//    "RON" -> rates.rON
//    "SEK" -> rates.sEK
//    "IDR" -> rates.iDR
//    "INR" -> rates.iNR
//    "BRL" -> rates.bRL
//    "RUB" -> rates.rUB
//    "HRK" -> rates.hRK
//    "JPY" -> rates.jPY
//    "THB" -> rates.tHB
//    "CHF" -> rates.cHF
//    "SGD" -> rates.sGD
//    "PLN" -> rates.pLN
//    "BGN" -> rates.bGN
//    "CNY" -> rates.cNY
//    "NOK" -> rates.nOK
//    "NZD" -> rates.nZD
//    "ZAR" -> rates.zAR
//    "USD" -> rates.uSD
//    "MXN" -> rates.mXN
//    "ILS" -> rates.iLS
//    "GBP" -> rates.gBP
//    "KRW" -> rates.kRW
//    "MYR" -> rates.mYR
//    else -> null
//}
