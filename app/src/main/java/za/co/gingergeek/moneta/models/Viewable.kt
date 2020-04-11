package za.co.gingergeek.moneta.models

import androidx.databinding.BaseObservable

abstract class Viewable : BaseObservable() {
    companion object {
        const val typeStoredExchangeRate: Int = 0
        const val typeHistoricalExchangeRate: Int = 1
    }

    abstract fun getType(): Int
}