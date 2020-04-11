package za.co.gingergeek.moneta.ui.home

import androidx.fragment.app.Fragment
import za.co.gingergeek.moneta.ui.BaseFragment
import za.co.gingergeek.moneta.ui.shared.recyclerview.viewmodels.SavedExchangeRateViewModel
import za.co.gingergeek.moneta.ui.shared.recyclerview.Presenter

class HomePresenter(val fragment: Fragment) : Presenter(fragment) {
    private var deleteCallback: (SavedExchangeRateViewModel) -> Unit = {}

    override fun deleteStoredExchangeRate(storedExchangeRate: SavedExchangeRateViewModel) {
        if (fragment is HomeFragment) {
            fragment.viewModel.deleteStoredExchangeRate(storedExchangeRate.isoCode) {
                deleteCallback.invoke(storedExchangeRate)
            }
        }
    }

    fun setDeleteCallback(callback: (SavedExchangeRateViewModel) -> Unit) {
        deleteCallback = callback
    }
}