package za.co.gingergeek.moneta.ui.currencydetail

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import za.co.gingergeek.moneta.models.ExchangeRateHistory

class CurrencyDetailViewModel : ViewModel() {

    private lateinit var currencyDetailRepository: CurrencyDetailRepository
    private lateinit var isoCode: String

    val exchangeDescription: MutableLiveData<String> = MutableLiveData<String>()
    val exchangeRate: MutableLiveData<String> = MutableLiveData<String>()
    val exchangeRateHistory: MutableLiveData<List<ExchangeRateHistory>> = MutableLiveData<List<ExchangeRateHistory>>()


    fun initialize(context: Context, isoCode: String) {
        this.currencyDetailRepository = CurrencyDetailRepository(context)
        this.isoCode = isoCode
        setExchangeRateDetails()
        populateHistoryData(isoCode)
    }

    fun refreshData() {
        populateHistoryData(isoCode)
    }

    private fun populateHistoryData(isoCode: String) {
        currencyDetailRepository.populateExchangeRateHistory(isoCode, exchangeRateHistory)
    }

    private fun setExchangeRateDetails() {
        currencyDetailRepository.populateExchangeRate(isoCode, exchangeDescription, exchangeRate)
    }
}