package za.co.gingergeek.moneta

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
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
        val latestTimestamp: Long = SharedPreferenceHelper
            .getExchangeRatesLastUpdatedTimestamp(this)
        /* The reason why notifications were not working was because the
        * app does a network request on first startup only, I have modified the code
        * to perform a network request every minute for quick testing */
        if (latestTimestamp == -1L ||
            System.currentTimeMillis() - latestTimestamp >= 60000L) {
            api?.fetchExchangeRates { response ->
                response?.run {
                    progress_text.text = getString(R.string.startup_fetching_data)
                    startupSyncOccurred = true
                    startActivityIfCountDown()
                } ?: run {
                    showNoNetworkText()
                }
            }
        } else {
            startActivityIfCountDown()
        }
    }

    private fun updateCurrenciesIfRequired() {
        val latestTimestamp: Long = SharedPreferenceHelper
            .getExchangeRatesLastUpdatedTimestamp(this)
        if (latestTimestamp == -1L ||
            System.currentTimeMillis() - latestTimestamp >= 60000L) {
            api?.fetchCurrencies { response ->
                response?.run {
                    progress_text.text = getString(R.string.startup_fetching_data)
                    startupSyncOccurred = true
                    startActivityIfCountDown()
                } ?: run {
                    showNoNetworkText()
                }
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

    private fun showNoNetworkText(): Boolean {
        progress_text.text = getString(R.string.startup_no_network_connection)
        progress_bar.visibility = View.GONE
        return Handler().postDelayed({ finish() }, 4000)
    }
}