package za.co.gingergeek.moneta.net

data class ExchangeRateResponse(var disclaimer: String, var license: String, var timestamp: Long,
                                var base: String, var rates: Map<String, Float>)