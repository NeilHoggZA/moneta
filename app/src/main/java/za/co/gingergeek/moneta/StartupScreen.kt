package za.co.gingergeek.moneta

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.activity_startup.*
import za.co.gingergeek.moneta.net.OpenExchangeRatesAPI
import za.co.gingergeek.moneta.helpers.SharedPreferenceHelper
import java.util.concurrent.CountDownLatch
import javax.inject.Inject

class StartupScreen : AppCompatActivity() {

    private val countDownLatch = CountDownLatch(2)
    private var startupSyncOccurred = false
    private var isoCode: String? = null

    var api: OpenExchangeRatesAPI? = null
        @Inject set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as MainApplication).applicationComponent?.inject(this)
        setContentView(R.layout.activity_startup)
        init()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        intent.extras?.let {
            isoCode = it.getString("iso_code") ?: ""
        }
    }

    private fun init() {
        updateCurrenciesIfRequired()
        updateExchangeRatesIfRequired()
    }

    private fun updateExchangeRatesIfRequired() {
        if (SharedPreferenceHelper.getExchangeRatesLastUpdatedTimestamp(this) == -1L) {
            api?.fetchExchangeRates {
                progress_text.text = getString(R.string.startup_fetching_data)
                startupSyncOccurred = true
                startActivityIfCountDown()
            }
        } else {
            startActivityIfCountDown()
        }
    }

    private fun updateCurrenciesIfRequired() {
        if (SharedPreferenceHelper.getCurrenciesLastUpdatedTimestamp(this) == -1L) {
            api?.fetchCurrencies {
                progress_text.text = getString(R.string.startup_fetching_data)
                startupSyncOccurred = true
                startActivityIfCountDown()
            }
        } else {
            startActivityIfCountDown()
        }
    }

    private fun startActivityIfCountDown() {
        countDownLatch.countDown()
        if (countDownLatch.count == 0L) {
            if (startupSyncOccurred) {
                SharedPreferenceHelper.saveLastSyncedTimestamp(this, System.currentTimeMillis())
            }

            val intent = Intent(this@StartupScreen, MainActivity::class.java)
            intent.putExtra("iso_code", isoCode)
            startActivity(intent)
            finish()
        }
    }
}