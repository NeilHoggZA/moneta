package za.co.gingergeek.moneta.room

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert

import org.junit.Test
import org.junit.runner.RunWith

import za.co.gingergeek.moneta.models.Currency

@RunWith(AndroidJUnit4::class)
class CurrencyDaoTests : BaseDaoTest() {

    @Test
    fun testTransactions() {
        val dao = database.currencyDao()
        dao.add(Currency("TEST", "My Test Currency"))

        val currency: Currency? = dao.findByIsoCode("TEST")
        Assert.assertNotNull(currency)

        currency?.let {
            dao.delete(it)
        }

        Assert.assertNull(dao.findByIsoCode("TEST"))
    }

    @Test
    fun testBulkInsert() {
        val dao = database.currencyDao()
        val currencies = listOf(Currency("T1", "T1 Test"), Currency("T2", "T2 Test"))
        dao.addAll(currencies)

        val storedCurrencies = dao.loadAll()
        assert(storedCurrencies.contains(currencies[0]))
        assert(storedCurrencies.contains(currencies[1]))
    }
}
