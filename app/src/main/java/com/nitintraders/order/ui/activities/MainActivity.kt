package com.nitintraders.order.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.nitintraders.order.R
import com.nitintraders.order.data.BeltOrder
import com.nitintraders.order.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding

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
}
