package za.co.gingergeek.moneta.room

import android.content.Context
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry

import org.junit.Before

open class BaseDaoTest {
    lateinit var database: AppRoomDatabase
    lateinit var context: Context

    @Before
    fun setupDatabase() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.databaseBuilder(
            context.applicationContext,
            AppRoomDatabase::class.java, "moneta-room-database"
        ).fallbackToDestructiveMigration().build()
    }
}
