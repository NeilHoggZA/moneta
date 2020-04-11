package za.co.gingergeek.moneta.helpers

object CurrencyConversionHelper {

    fun convertUsdToCurrency(amount: Float, exchangeRate: Float): Float {
        val markup = if (amount > 199.99) exchangeRate * 0.04f else exchangeRate * 0.07f
        return amount * (exchangeRate - markup)
    }
}