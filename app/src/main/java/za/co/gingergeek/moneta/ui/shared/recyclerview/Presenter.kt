package za.co.gingergeek.moneta.ui.shared.recyclerview

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import za.co.gingergeek.moneta.R
import za.co.gingergeek.moneta.ui.shared.recyclerview.viewmodels.ExchangeRateHistoryViewModel
import za.co.gingergeek.moneta.ui.shared.recyclerview.viewmodels.SavedExchangeRateViewModel

interface RecyclerPresenter

open class Presenter(private val fragment: Fragment): RecyclerPresenter {
    open fun onStoredExchangeRateClick(storedExchangeRate: SavedExchangeRateViewModel) {
        val bundle = Bundle()
        bundle.putString("iso_code", storedExchangeRate.isoCode)
        fragment.findNavController().navigate(R.id.action_homeFragment_to_currencyDetailFragment, bundle)
    }

    open fun deleteStoredExchangeRate(storedExchangeRate: SavedExchangeRateViewModel) {}
    open fun onHistoricalExchangeRateClick(historicalExchangeRate: ExchangeRateHistoryViewModel) {}
}