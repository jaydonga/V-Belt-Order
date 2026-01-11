package com.nitintraders.order.ui.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.nitintraders.order.R
import com.nitintraders.order.data.BeltOrder
import com.nitintraders.order.databinding.ActivityMainBinding
import com.nitintraders.order.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Locale

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navHostFragment = supportFragmentManager.findFragmentById(
            R.id.main_nav_host_fragment
        ) as NavHostFragment
        navController = navHostFragment.navController

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        lifecycleScope.launch {
            viewModel.readSelectedLanguage.flowWithLifecycle(lifecycle, Lifecycle.State.CREATED).collect {
                setAppLanguage(it)
            }
        }
    }

    fun navigateToCreateNewOrder(beltOrder: BeltOrder? = null) {
        if (navController.currentDestination?.id == R.id.homeScreen) {
            navController.navigate(R.id.action_homeScreen_to_createNewOrder)
        } else if (navController.currentDestination?.id == R.id.viewOrders) {
            val args = Bundle()
            args.putParcelable(BeltOrder::class.simpleName, beltOrder)
            navController.navigate(R.id.action_viewOrders_to_createNewOrder, args)
        }
    }

    fun navigateToViewOrders() {
        if (navController.currentDestination?.id == R.id.createNewOrder) {
            navController.navigate(R.id.action_createNewOrder_to_viewOrders)
        } else if (navController.currentDestination?.id == R.id.homeScreen) {
            navController.navigate(R.id.action_homeScreen_to_viewOrders)
        }
    }

    fun setAppLanguage(languageCode: String) {
        val appLocale = LocaleListCompat.forLanguageTags(languageCode)
        AppCompatDelegate.setApplicationLocales(appLocale)
    }
}
