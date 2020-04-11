package za.co.gingergeek.moneta.ui

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import za.co.gingergeek.moneta.MainApplication
import za.co.gingergeek.moneta.room.AppRoomDatabase
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

abstract class BaseRepository(context: Context) : CoroutineScope {
    var database: AppRoomDatabase? = null
        @Inject set

    init {
        (context.applicationContext as MainApplication).applicationComponent?.inject(this)
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

}