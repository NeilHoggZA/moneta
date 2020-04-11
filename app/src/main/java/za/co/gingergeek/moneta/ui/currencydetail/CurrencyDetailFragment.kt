package za.co.gingergeek.moneta.ui.currencydetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_currency_detail.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import za.co.gingergeek.moneta.R
import za.co.gingergeek.moneta.databinding.FragmentCurrencyDetailBinding
import za.co.gingergeek.moneta.models.events.SyncCompletedEvent
import za.co.gingergeek.moneta.ui.BaseFragment

class CurrencyDetailFragment : BaseFragment() {

    private lateinit var binding: FragmentCurrencyDetailBinding
    private val viewModel: CurrencyDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_currency_detail, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        context?.let { context ->
            arguments?.let { args ->
                val isoCode = args.getString("iso_code") ?: ""
                binding.viewModel = viewModel
                binding.lifecycleOwner = viewLifecycleOwner
                viewModel.initialize(context, isoCode)

                val adapter = setupRecycler(
                    recycler,
                    LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                )
                viewModel.exchangeRateHistory.observe(viewLifecycleOwner, Observer {
                    adapter.refreshData(it.reversed())
                })
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    override fun getTitle(): String {
        return getString(R.string.currency_detail_fragment_label)
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onEvent(event: SyncCompletedEvent) {
        viewModel.refreshData()
    }
}