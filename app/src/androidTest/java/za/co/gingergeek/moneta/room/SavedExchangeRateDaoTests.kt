package za.co.gingergeek.moneta.room

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert

import org.junit.Test
import org.junit.runner.RunWith

import za.co.gingergeek.moneta.models.SavedExchangeRate

@RunWith(AndroidJUnit4::class)
class SavedExchangeRateDaoTests : BaseDaoTest() {

    @Test
    fun testTransactions() {
        val dao = database.savedExchangeRatesDao()
        dao.add(SavedExchangeRate("ABC", 1.02354f, 1.0f))
        dao.add(SavedExchangeRate("XYZ", 18.02354f, 18.05f))

        val savedExchangeRates: List<SavedExchangeRate> = dao.loadAll()
        Assert.assertNotNull(savedExchangeRates)
        assert(savedExchangeRates.isNotEmpty())

        savedExchangeRates.forEach {
            dao.delete(it.isoCode)
        }

        assert(dao.loadAll().isEmpty())
    }
}
