package za.co.gingergeek.moneta

import org.junit.Assert
import org.junit.Test
import za.co.gingergeek.moneta.helpers.CurrencyConversionHelper


class ConversionTest {

    @Test
    fun testExchangeRateConversion7PercentMarkup() {
        val conversionResult = CurrencyConversionHelper.convertUsdToCurrency(25f, 18.05832f)
        Assert.assertEquals(419.85593f, conversionResult)
    }

    @Test
    fun testExchangeRateConversion4PercentMarkup() {
        val conversionResult = CurrencyConversionHelper.convertUsdToCurrency(200f, 18.05832f)
        Assert.assertEquals(3467.19744f, conversionResult)
    }
}