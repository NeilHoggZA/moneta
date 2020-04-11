package za.co.gingergeek.moneta.ui.converter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_converter.*
import kotlinx.android.synthetic.main.fragment_converter.selection
import za.co.gingergeek.moneta.R
import za.co.gingergeek.moneta.databinding.FragmentConverterBinding
import za.co.gingergeek.moneta.extensions.fadeIn
import za.co.gingergeek.moneta.extensions.fadeOut
import za.co.gingergeek.moneta.ui.BaseFragment
import za.co.gingergeek.moneta.ui.addcurrency.adapter.SpinnerAdapter

class ConverterFragment : BaseFragment() {

    private lateinit var binding: FragmentConverterBinding
    private val viewModel: ConverterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_converter, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.let { context ->
            binding.viewModel = viewModel
            binding.lifecycleOwner = viewLifecycleOwner
            viewModel.initialize(context)

            val adapter = SpinnerAdapter(context)
            viewModel.currencies.observe(viewLifecycleOwner, Observer { currencies ->
                adapter.refreshData(currencies)
                selection.adapter = adapter
            })

            convert_amount_input.addTextChangedListener {
                if (it?.isEmpty() == true) {
                    result_card.fadeOut()
                }
            }

            convert_button.setOnClickListener {
                performConversion(adapter, context)
            }
        }
    }

    override fun getTitle(): String {
        return getString(R.string.currency_converter_fragment_label)
    }

    private fun performConversion(
        adapter: SpinnerAdapter,
        context: Context
    ) {
        val amount = convert_amount_input.text.toString()
        if (amount.isNotEmpty()) {
            val currency = adapter.data[selection.selectedItemPosition]
            viewModel.convertedValue.observe(viewLifecycleOwner, Observer {
                result_card.fadeIn()
            })
            viewModel.convertCurrency(currency)
        } else {
            Toast.makeText(
                context, getString(R.string.converter_screen_amount_not_entered),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}