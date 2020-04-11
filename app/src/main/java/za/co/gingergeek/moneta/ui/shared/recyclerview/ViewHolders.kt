package za.co.gingergeek.moneta.ui.shared.recyclerview

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import za.co.gingergeek.moneta.BR
import za.co.gingergeek.moneta.models.*
import za.co.gingergeek.moneta.ui.shared.recyclerview.viewmodels.ExchangeRateHistoryViewModel
import za.co.gingergeek.moneta.ui.shared.recyclerview.viewmodels.SavedExchangeRateViewModel

abstract class BaseViewHolder(binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
    abstract fun bind(viewableData: Viewable, presenter: RecyclerPresenter)
}

class ExchangeRateItemViewHolder(private val binding: ViewDataBinding) : BaseViewHolder(binding) {
    override fun bind(viewableData: Viewable, presenter: RecyclerPresenter) {
        if (viewableData is SavedExchangeRate) {
            binding.setVariable(BR.presenter, presenter)
            binding.setVariable(BR.storedExchangeRate,
                SavedExchangeRateViewModel(
                    viewableData
                )
            )
            binding.executePendingBindings()
        }
    }
}

class HistoricalExchangeRateItemViewHolder(private val binding: ViewDataBinding) : BaseViewHolder(binding) {
    override fun bind(viewableData: Viewable, presenter: RecyclerPresenter) {
        if (viewableData is ExchangeRateHistory) {
            binding.setVariable(BR.presenter, presenter)
            binding.setVariable(BR.historicalExchangeRate,
                ExchangeRateHistoryViewModel(
                    viewableData
                )
            )
            binding.executePendingBindings()
        }
    }
}