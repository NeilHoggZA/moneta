package za.co.gingergeek.moneta

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController

import kotlinx.android.synthetic.main.activity_main.*
import za.co.gingergeek.moneta.ui.home.HomeFragment
import za.co.gingergeek.moneta.ui.shared.ActivityCallback

class MainActivity : AppCompatActivity(),
    ActivityCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        intent.extras?.let {
            val isoCode = it.getString("iso_code") ?: ""
            if (isoCode.isNotEmpty()) {
                findNavController(R.id.nav_host_fragment).navigate(R.id.currencyDetailFragment, it)
            }
        }
    }

    override fun onFragmentChange(fragment: Fragment) {
        val showNavIcon = fragment !is HomeFragment
        supportActionBar?.setDisplayHomeAsUpEnabled(showNavIcon)
        supportActionBar?.setDisplayShowHomeEnabled(showNavIcon)
    }

    override fun onSupportNavigateUp() =
        findNavController(R.id.nav_host_fragment).navigateUp()

    fun setToolbarTitle(title: String = "") {
        toolbar_title.text = title
    }
}
