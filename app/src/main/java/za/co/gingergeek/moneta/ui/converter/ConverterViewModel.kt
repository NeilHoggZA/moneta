package za.co.gingergeek.moneta.ui.converter

import android.content.Context
import androidx.databinding.Bindable
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.android.synthetic.main.fragment_converter.*
import za.co.gingergeek.moneta.extensions.fadeIn
import za.co.gingergeek.moneta.helpers.CurrencyConversionHelper
import za.co.gingergeek.moneta.models.Currency
import java.text.DecimalFormat

class ConverterViewModel : ViewModel() {

    private var context: Context? = null
    private lateinit var converterRepository: ConverterRepository


    val currencies: MutableLiveData<List<Currency>> by lazy {
        MutableLiveData<List<Currency>>().also {
            loadStoredCurrencies(it)
        }
    }

    val convertedValue: MutableLiveData<String> = MutableLiveData()
    val amount: MutableLiveData<String> = MutableLiveData()
    val markupPercentage: MutableLiveData<String> by lazy {
        MutableLiveData<String>().also {
            it.value = "7%"
        }
    }

    fun initialize(context: Context) {
        this.context = context
        this.converterRepository = ConverterRepository(context)
    }

    fun convertCurrency(currency: Currency) {
        getExchangeRate(currency.isoCode)  { rate ->
            val deductedResult = CurrencyConversionHelper.convertUsdToCurrency(amount.value?.toFloat() ?: 0f, rate)
            val formattedResult =
                "${DecimalFormat("##.##").format(deductedResult)} ${currency.isoCode}"
            setMarkupPercentage(amount.value ?: "0")
            convertedValue.value = formattedResult
        }
    }

    private fun getExchangeRate(isoCode: String, callback: (Float) -> Unit) {
        converterRepository.loadExchangeRateForIsoCode(isoCode, callback)
    }

    private fun loadStoredCurrencies(currencies: MutableLiveData<List<Currency>>) {
        converterRepository.loadAllCurrencies {
            currencies.value = it
        }
    }

    private fun setMarkupPercentage(amount: String) {
        markupPercentage.value = if (amount.toFloat() > 199.99) "4%" else "7%"
    }
}