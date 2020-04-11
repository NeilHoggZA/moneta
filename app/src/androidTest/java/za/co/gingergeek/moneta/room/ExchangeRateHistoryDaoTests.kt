package za.co.gingergeek.moneta.room

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert

import org.junit.Test
import org.junit.runner.RunWith

import za.co.gingergeek.moneta.models.ExchangeRateHistory
import za.co.gingergeek.moneta.room.BaseDaoTest
import java.util.*

@RunWith(AndroidJUnit4::class)
class ExchangeRateHistoryDaoTests : BaseDaoTest() {

    @Test
    fun testTransactions() {
        val dao = database.exchangeRateHistoryDao()
        dao.addAll(getExchangeRateHistory())
        dao.add(ExchangeRateHistory(
            UUID.randomUUID().toString(),
            System.currentTimeMillis() + 120000,
            "ABC",
            0.454f
        ))

        val exchangeRate: List<ExchangeRateHistory>? = dao.findByISOCode("ABC")
        Assert.assertNotNull(exchangeRate)

        dao.delete("ABC")
        assert(dao.findByISOCode("ABC").isEmpty())
    }

    @Test
    fun testBulkInsertAndFetch() {
        val dao = database.exchangeRateHistoryDao()
        val exchangeRates = getExchangeRateHistory()
        dao.addAll(exchangeRates)

        val storedCurrencies = dao.loadAll()
        assert(storedCurrencies.contains(exchangeRates[0]))
        assert(storedCurrencies.contains(exchangeRates[1]))
    }

    private fun getExchangeRateHistory(): List<ExchangeRateHistory> {
        return listOf(ExchangeRateHistory(
            UUID.randomUUID().toString(),
            System.currentTimeMillis(),
            "ABC",
            1.02354f
        ), ExchangeRateHistory(
            UUID.randomUUID().toString(),
            System.currentTimeMillis() + 60000,
            "ABC",
            1.54f
        ))
    }
}
