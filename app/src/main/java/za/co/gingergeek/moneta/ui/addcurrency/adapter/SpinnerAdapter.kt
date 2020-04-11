package za.co.gingergeek.moneta.ui.addcurrency.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import za.co.gingergeek.moneta.R
import za.co.gingergeek.moneta.models.Currency

class SpinnerAdapter(context: Context) : BaseAdapter() {

    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)
    val data: MutableList<Currency> = mutableListOf()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val viewHolder: ViewHolder?
        var view: View? = convertView
        if (view == null) {
            view = layoutInflater.inflate(R.layout.item_currency, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            viewHolder = view.tag as ViewHolder?
        }

        viewHolder?.name?.text = data[position].name
        viewHolder?.isoCode?.text = data[position].isoCode
        return view
    }

    override fun getItem(position: Int): Currency = data[position]
    override fun getItemId(position: Int): Long = 0
    override fun getCount(): Int = data.size

    fun refreshData(updatedData: List<Currency>) {
        data.clear()
        data.addAll(updatedData)
        notifyDataSetChanged()
    }

    private class ViewHolder(view: View) {
        var isoCode: TextView? = view.findViewById(R.id.iso_code)
        var name: TextView? = view.findViewById(R.id.currencyName)
    }
}