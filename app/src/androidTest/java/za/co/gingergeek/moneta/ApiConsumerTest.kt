package za.co.gingergeek.moneta

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import za.co.gingergeek.moneta.net.OpenExchangeRatesConsumer

@RunWith(AndroidJUnit4::class)
class ApiConsumerTest {
    private lateinit var openExchangeRatesConsumer: OpenExchangeRatesConsumer

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        openExchangeRatesConsumer = OpenExchangeRatesConsumer(context,
            BuildConfig.BASE_URL_SCHEMA,
            BuildConfig.API_KEY)
    }

    @Test
    fun fetchCurrencies() {
        openExchangeRatesConsumer.fetchCurrencies { apiResponse ->
            apiResponse?.let {
                assert(it.successful)
                assert(it.value?.isNotEmpty() == true)
            }
        }
    }

    @Test
    fun fetchExchangeRates() {
        openExchangeRatesConsumer.fetchExchangeRates { apiResponse ->
            apiResponse?.let {
                assert(it.successful)
                Assert.assertNotNull(it.value)
                assert(it.value?.rates?.isNotEmpty() == true)
            }
        }
    }
}