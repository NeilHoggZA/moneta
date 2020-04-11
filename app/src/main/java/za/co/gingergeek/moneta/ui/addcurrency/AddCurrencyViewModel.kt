package za.co.gingergeek.moneta.ui.addcurrency

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.android.synthetic.main.fragment_add_currency.*
import za.co.gingergeek.moneta.models.Currency

class AddCurrencyViewModel : ViewModel() {

    private lateinit var addCurrencyRepository: AddCurrencyRepository
    private var context: Context? = null

    fun initialize(context: Context) {
        this.context = context
        this.addCurrencyRepository = AddCurrencyRepository(context)
    }

    val currencies: MutableLiveData<List<Currency>> by lazy {
        MutableLiveData<List<Currency>>().also {
            loadStoredCurrencies(it)
        }
    }

    val warningRate: MutableLiveData<String> = MutableLiveData()

    fun setWarningRate(currency: Currency) {
        loadExchangeRate(currency.isoCode) { rate ->
            warningRate.value = rate.toString()
        }
    }

    fun loadExchangeRate(isoCode: String, callback: (Float) -> Unit) {
        addCurrencyRepository.loadExchangeRateForIsoCode(isoCode, callback)
    }

    fun saveIfDataIsValid(currency: Currency, successCallback: () -> Unit, failedCallback: () -> Unit) {
        if (warningRate.value?.isNotEmpty() == true) {
            storeMonitoredExchangeRate(currency, warningRate.value?.toFloat() ?: 0f)
            successCallback.invoke()
        } else {
            failedCallback.invoke()
        }
    }

    private fun loadStoredCurrencies(currencies: MutableLiveData<List<Currency>>) {
        addCurrencyRepository.loadAllCurrencies {
            currencies.value = it
        }
    }

    private fun storeMonitoredExchangeRate(currency: Currency, minimumRate: Float) {
        addCurrencyRepository.storeMonitoredExchangeRate(currency, minimumRate)
    }
}