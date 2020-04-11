package za.co.gingergeek.moneta.helpers

import android.app.Activity
import android.content.res.Configuration
import android.os.Build
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.core.content.ContextCompat
import za.co.gingergeek.moneta.R

object ApplicationHelper {
    @JvmStatic
    fun setStatusBarColor(activity: Activity) {
        val statusBarLight = getMode(activity)
        val color = if (statusBarLight) R.color.black else R.color.white

        try {
            val window = activity.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = ContextCompat.getColor(activity, color)
            window.navigationBarColor = ContextCompat.getColor(activity, color)
            setDecorView(window, statusBarLight)

        } catch (e: Exception) {
            Log.e(ApplicationHelper::class.java.simpleName, "ERROR", e)
        }
    }

    @JvmStatic
    fun setNavigationBarColor(activity: Activity, color: Int) {
        try {
            val window = activity.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.navigationBarColor = color

            val lightMode = getMode(activity)
            setDecorView(window, lightMode)
        } catch (e: Exception) {
            Log.e(ApplicationHelper::class.java.simpleName, "ERROR", e)
        }
    }

    private fun setDecorView(window: Window, lightMode: Boolean) {
        val decorView = window.decorView
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (lightMode) {
                decorView.systemUiVisibility =
                    decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv() and View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR.inv()
            } else {
                decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_VISIBLE or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (lightMode) {
                decorView.systemUiVisibility =
                    decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            } else {
                decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_VISIBLE or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
    }

    private fun getMode(activity: Activity): Boolean {
        return when (activity.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO -> false
            Configuration.UI_MODE_NIGHT_YES -> true
            else -> false
        }
    }
}