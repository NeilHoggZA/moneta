package za.co.gingergeek.moneta.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import za.co.gingergeek.moneta.room.AppRoomDatabase
import javax.inject.Singleton

@Module(includes = [ApplicationModule::class])
class DatabaseModule {
    @Singleton
    @Provides
    fun providesBackendApi(context: Context): AppRoomDatabase? {
        return Room.databaseBuilder(
            context.applicationContext,
            AppRoomDatabase::class.java, "moneta-room-database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}