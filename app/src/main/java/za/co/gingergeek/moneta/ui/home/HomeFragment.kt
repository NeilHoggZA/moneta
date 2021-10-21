package za.co.gingergeek.moneta.ui.home

import android.content.res.Configuration
import android.os.Bundle
import android.view.*
import android.view.View.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import kotlinx.android.synthetic.main.fragment_home.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import za.co.gingergeek.moneta.R
import za.co.gingergeek.moneta.databinding.FragmentHomeBinding
import za.co.gingergeek.moneta.extensions.fadeIn
import za.co.gingergeek.moneta.extensions.fadeOut
import za.co.gingergeek.moneta.models.SavedExchangeRate
import za.co.gingergeek.moneta.models.events.SyncCompletedEvent
import za.co.gingergeek.moneta.ui.BaseFragment
import za.co.gingergeek.moneta.ui.shared.recyclerview.GenericRecyclerAdapter

class HomeFragment : BaseFragment() {

    private lateinit var binding: FragmentHomeBinding
    val viewModel: HomeViewModel by viewModels()

    //private lateinit var savedExchangeRates: List<SavedExchangeRate>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        context?.let { context ->
            binding.viewmodel = viewModel
            binding.lifecycleOwner = viewLifecycleOwner
            viewModel.initialize(context)

            setupRecyclerView()
            setupBottomAppBar()
            add_currency_fab.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_addCurrencyFragment)
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

    override fun onResume() {
        super.onResume()
        viewModel.refreshData()
    }

    override fun getTitle(): String {
        return getString(R.string.home_fragment_label)
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onEvent(event: SyncCompletedEvent) {
        viewModel.refreshData()
    }

    private fun setupRecyclerView() {
        val layoutManager = getLayoutManager()
        val presenter = HomePresenter(this)
        val adapter = GenericRecyclerAdapter(context, presenter)
        presenter.setDeleteCallback {
            //adapter.removeItem(viewModel.findSavedRate(it.isoCode)) -> redundant
            //toggleViews(adapter.isEmpty()) -> redundant
            // all we need to do is refresh the data when an item is deleted
            viewModel.refreshData()
        }

        recycler.addOnScrollListener(getScrollListener())
        recycler.layoutManager = layoutManager
        recycler.adapter = adapter
        viewModel.savedRates.observe(viewLifecycleOwner, {
            adapter.refreshData(it)
            toggleViews(it.isEmpty())
            setupBarChart(it)
        })
    }

    private fun setupBarChart(savedExchangeRates: List<SavedExchangeRate>) {
        if (savedExchangeRates.isNotEmpty()) {
            val barChartData = createChartData(savedExchangeRates)
            configureChartAppearance(savedExchangeRates)
            prepareChartData(barChartData)
        }
    }

    /**
     *
     */
    private fun createChartData(savedExchangeRates: List<SavedExchangeRate>): BarData {
        val barData = arrayListOf<BarEntry>()
        barData.addAll(savedExchangeRates.mapIndexed { i, value ->
            BarEntry(i.toFloat(), value.exchangeRate)
        })
        val set = BarDataSet(barData, "Currency")
        val dataSets = arrayListOf<IBarDataSet>()
        dataSets.add(set)
        return BarData(dataSets)
    }

    /**
     *
     */
    private fun configureChartAppearance(savedExchangeRates: List<SavedExchangeRate>) {
        bar_chart.description.isEnabled = false
        bar_chart.setDrawValueAboveBar(false)

        val xAxis: XAxis = bar_chart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f
        xAxis.setDrawAxisLine(false)
        xAxis.setDrawGridLines(false)

        // fix the index issue here
        xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return savedExchangeRates[value.toInt()].isoCode
            }
        }

        val axisLeft = bar_chart.axisLeft
        axisLeft.setDrawGridLines(false)
        axisLeft.granularity = 10f
        axisLeft.axisMinimum = 0f

        val axisRight = bar_chart.axisRight
        axisRight.setDrawGridLines(false)
        axisRight.granularity = 10f
        axisRight.axisMinimum = 0f
    }

    /**
     *
     */
    private fun prepareChartData(data: BarData) {
        data.setValueTextSize(12f)
        bar_chart.data = data
        bar_chart.invalidate()
    }

    private fun getLayoutManager(): RecyclerView.LayoutManager {
        val orientation = resources.configuration.orientation
        return if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        } else {
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        }
    }

    private fun setupBottomAppBar() {
        bottom_app_bar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_converter -> findNavController().navigate(R.id.action_homeFragment_to_converterFragment)
            }
            true
        }
    }

    private fun getScrollListener(): RecyclerView.OnScrollListener {
        return object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    toggleLastUpdatedTime(false)
                } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    toggleLastUpdatedTime(recycler.computeVerticalScrollOffset() == 0)
                }
            }

            private fun toggleLastUpdatedTime(show: Boolean) {
                if (show) {
                    last_updated_image.fadeIn()
                    last_updated.fadeIn()
                } else {
                    last_updated_image.fadeOut()
                    last_updated.fadeOut()
                }
            }
        }
    }

    /**
     * If I do this via DataBinding through the ViewModel there's an ugly flash of the no data
     * view before the content shows up. This state also causes problems with animations happening
     * simultaneously so I had to come up with this solution. The delay stops the show animation
     */
    private fun toggleViews(isEmpty: Boolean) {
        if (isEmpty) {
            last_updated.visibility = INVISIBLE
            last_updated_image.visibility = INVISIBLE
            no_data_layout.visibility = VISIBLE
            arrow.visibility = VISIBLE
            bar_chart.visibility = GONE
        } else {
            bar_chart.visibility = VISIBLE
            last_updated.visibility = VISIBLE
            last_updated_image.visibility = VISIBLE
            no_data_layout.visibility = INVISIBLE
            arrow.visibility = INVISIBLE
        }
    }
}
