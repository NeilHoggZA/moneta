package za.co.gingergeek.moneta.ui.shared.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import za.co.gingergeek.moneta.R
import za.co.gingergeek.moneta.models.Viewable

class GenericRecyclerAdapter(context: Context?, private val presenter: RecyclerPresenter) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private val data: MutableList<Viewable> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == Viewable.typeStoredExchangeRate) {
            ExchangeRateItemViewHolder(
                DataBindingUtil.inflate(inflater, R.layout.item_exchange_rate_layout, parent, false))
        } else {
            HistoricalExchangeRateItemViewHolder(
                DataBindingUtil.inflate(inflater, R.layout.item_historical_exchange_rate_layout, parent, false))
        }
    }

    override fun getItemCount(): Int = data.size
    override fun getItemViewType(position: Int): Int = data[position].getType()

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is BaseViewHolder) {
            holder.bind(data[position], presenter)
        }
    }

    /**
     * This probably worked but now that the data is
     * also used to display a bar chart, we need a single source of truth
     * to maintain consistency between the list of rates and the bar chart
     */
    fun removeItem(viewable: Viewable?): Int {
        val position = data.indexOf(viewable)
        if (position != -1) {
            data.removeAt(position)
            notifyItemRemoved(position)
        }
        return position
    }

    /**
     * This method should be deleted
     */
    fun isEmpty(): Boolean = data.isEmpty()

    fun refreshData(updatedData: List<Viewable>) {
        data.clear()
        data.addAll(updatedData)
        notifyDataSetChanged()
    }
}