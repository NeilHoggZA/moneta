package za.co.gingergeek.moneta.room

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert

import org.junit.Test
import org.junit.runner.RunWith

import za.co.gingergeek.moneta.models.WarningMessage

@RunWith(AndroidJUnit4::class)
class WarningMessageDaoTests : BaseDaoTest() {

    @Test
    fun testTransactions() {
        val dao = database.warningMessageDao()
        dao.add(WarningMessage("ABC", 1.02354f))
        dao.add(WarningMessage("XYZ", 18.02354f))

        val warningMessage: WarningMessage? = dao.loadMessageForCode("ABC")
        Assert.assertNotNull(warningMessage)

        warningMessage?.let {
            dao.delete(it.isoCode)
        }

        Assert.assertNull(dao.loadMessageForCode("ABC"))
    }
}
