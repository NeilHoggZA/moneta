package za.co.gingergeek.moneta.ui

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import za.co.gingergeek.moneta.MainActivity
import za.co.gingergeek.moneta.R
import za.co.gingergeek.moneta.ui.home.HomeFragment
import za.co.gingergeek.moneta.ui.shared.ActivityCallback
import za.co.gingergeek.moneta.ui.shared.recyclerview.GenericRecyclerAdapter
import za.co.gingergeek.moneta.ui.shared.recyclerview.Presenter
import za.co.gingergeek.moneta.helpers.ApplicationHelper


abstract class BaseFragment : Fragment() {
    private var activityCallback: ActivityCallback? = null

    abstract fun getTitle(): String

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ActivityCallback) {
            activityCallback = context
        }
    }

    override fun onResume() {
        super.onResume()
        activityCallback?.onFragmentChange(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (activity is MainActivity) {
            (activity as MainActivity).setToolbarTitle(getTitle())
        }

        setNavigationBarColor()
        return super.onViewCreated(view, savedInstanceState)
    }

    private fun setNavigationBarColor() {
        activity?.let { activity ->
            var modeColor = 0
            var accentColor = 0
            when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_NO -> {
                    accentColor = R.color.colorAccent
                    modeColor = R.color.white
                }
                Configuration.UI_MODE_NIGHT_YES -> {
                    accentColor = R.color.colorAccentDark
                    modeColor = R.color.night_mode_black
                }
                else -> {
                    modeColor = R.color.white
                }
            }
            val color = if (this is HomeFragment) accentColor else modeColor
            ApplicationHelper.setNavigationBarColor(
                activity as Activity,
                ContextCompat.getColor(activity, color)
            )
        }
    }

    fun setupRecycler(
        recyclerView: RecyclerView,
        layoutManager: RecyclerView.LayoutManager
    ): GenericRecyclerAdapter {
        val adapter = GenericRecyclerAdapter(context, Presenter(this))
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        return adapter
    }
}