package za.co.gingergeek.moneta.di

import dagger.Component
import za.co.gingergeek.moneta.MainApplication
import za.co.gingergeek.moneta.StartupScreen
import za.co.gingergeek.moneta.sync.SyncService
import za.co.gingergeek.moneta.ui.BaseRepository
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, NetModule::class, DatabaseModule::class])
interface ApplicationComponent {
    fun inject(component: StartupScreen?)
    fun inject(component: SyncService?)
    fun inject(component: BaseRepository?)
    fun inject(component: MainApplication?)
}