package za.co.gingergeek.moneta.ui.home

import android.content.res.Configuration
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kotlinx.android.synthetic.main.fragment_home.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import za.co.gingergeek.moneta.R
import za.co.gingergeek.moneta.databinding.FragmentHomeBinding
import za.co.gingergeek.moneta.extensions.fadeIn
import za.co.gingergeek.moneta.extensions.fadeOut
import za.co.gingergeek.moneta.models.events.SyncCompletedEvent
import za.co.gingergeek.moneta.ui.BaseFragment
import za.co.gingergeek.moneta.ui.shared.recyclerview.GenericRecyclerAdapter

class HomeFragment : BaseFragment() {

    private lateinit var binding: FragmentHomeBinding
    val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
            adapter.removeItem(viewModel.findSavedRate(it.isoCode))
            toggleViews(adapter.isEmpty())
        }

        recycler.addOnScrollListener(getScrollListener())
        recycler.layoutManager = layoutManager
        recycler.adapter = adapter
        viewModel.savedRates.observe(viewLifecycleOwner, Observer {
            adapter.refreshData(it)
            toggleViews(it.isEmpty())
        })
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
            last_updated.visibility = View.INVISIBLE
            last_updated_image.visibility = View.INVISIBLE
            no_data_layout.visibility = View.VISIBLE
            arrow.visibility = View.VISIBLE
        } else {
            last_updated.visibility = View.VISIBLE
            last_updated_image.visibility = View.VISIBLE
            no_data_layout.visibility = View.INVISIBLE
            arrow.visibility = View.INVISIBLE
        }
    }
}
