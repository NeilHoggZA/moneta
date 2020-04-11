package za.co.gingergeek.moneta.room

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert

import org.junit.Test
import org.junit.runner.RunWith

import za.co.gingergeek.moneta.models.ExchangeRate

@RunWith(AndroidJUnit4::class)
class ExchangeRateDaoTests : BaseDaoTest() {

    @Test
    fun testTransactions() {
        val dao = database.exchangeRateDao()
        dao.add(ExchangeRate("TEST", 18.02354f))

        val exchangeRate: ExchangeRate? = dao.findByISOCode("TEST")
        Assert.assertNotNull(exchangeRate)

        exchangeRate?.let {
            dao.delete(it)
        }

        Assert.assertNull(dao.findByISOCode("TEST"))
    }

    @Test
    fun testBulkInsert() {
        val dao = database.exchangeRateDao()
        val exchangeRates = listOf(ExchangeRate("T1", 1.29f), ExchangeRate("T2", 14.4f))
        dao.addAll(exchangeRates)

        val storedCurrencies = dao.loadAll()
        assert(storedCurrencies.contains(exchangeRates[0]))
        assert(storedCurrencies.contains(exchangeRates[1]))
    }
}
