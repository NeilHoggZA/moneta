package za.co.gingergeek.moneta.di

import android.content.Context
import dagger.Module
import dagger.Provides
import za.co.gingergeek.moneta.BuildConfig
import za.co.gingergeek.moneta.net.OpenExchangeRatesAPI
import za.co.gingergeek.moneta.net.OpenExchangeRatesConsumer
import javax.inject.Singleton

@Module(includes = [ApplicationModule::class])
class NetModule {
    @Singleton
    @Provides
    fun providesBackendApi(context: Context): OpenExchangeRatesAPI? {
        return OpenExchangeRatesConsumer(
            context,
            BuildConfig.BASE_URL_SCHEMA,
            BuildConfig.API_KEY
        )
    }
}