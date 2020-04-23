package za.co.gingergeek.moneta.net

interface OpenExchangeRatesAPI {
    fun fetchCurrencies(callback: (ApiResponse<Map<String, String>>?) -> Unit)
    fun fetchExchangeRates(callback: (ApiResponse<ExchangeRateResponse>?) -> Unit)
    fun dispose()
}