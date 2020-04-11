package za.co.gingergeek.moneta.ui.addcurrency

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_add_currency.*
import za.co.gingergeek.moneta.R
import za.co.gingergeek.moneta.databinding.FragmentAddCurrencyBinding
import za.co.gingergeek.moneta.models.Currency
import za.co.gingergeek.moneta.ui.BaseFragment
import za.co.gingergeek.moneta.ui.addcurrency.adapter.SpinnerAdapter
import za.co.gingergeek.moneta.helpers.KeyboardHelper

class AddCurrencyFragment : BaseFragment() {

    private lateinit var binding: FragmentAddCurrencyBinding
    private val viewModel: AddCurrencyViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_add_currency,
            container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        context?.let { context ->
            binding.viewmodel = viewModel
            binding.lifecycleOwner = viewLifecycleOwner
            viewModel.initialize(context)
            viewModel.currencies.observe(viewLifecycleOwner, Observer { currencies ->
                setupSpinner(context, currencies)
            })

            save_button.setOnClickListener {
                attemptToStoreData(context)
            }
        }
    }

    override fun getTitle(): String {
        return getString(R.string.add_currency_fragment_label)
    }

    private fun setupSpinner(
        context: Context,
        currencies: List<Currency>
    ) {
        val adapter = SpinnerAdapter(context)
        adapter.refreshData(currencies)
        selection.adapter = adapter
        selection.onItemSelectedListener = getOnItemSelectedListener(adapter)
    }

    private fun getOnItemSelectedListener(
        adapter: SpinnerAdapter
    ): AdapterView.OnItemSelectedListener {
        return object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewModel.setWarningRate(adapter.data[position])
            }
        }
    }

    private fun attemptToStoreData(context: Context) {
        viewModel.saveIfDataIsValid(selection.selectedItem as Currency, {
            KeyboardHelper.hideKeyboard(activity)
            findNavController().navigateUp()
        }) {
            Toast.makeText(
                context, getString(R.string.add_currency_field_not_populated),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
