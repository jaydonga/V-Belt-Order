package com.nitintraders.v_beltorder.ui.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.nitintraders.v_beltorder.R

class MainActivity : AppCompatActivity() {

    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        navHostFragment = supportFragmentManager.findFragmentById(R.id.main_nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

    }

    fun navigateToCreateNewOrder() {
        navController.navigate(R.id.action_homeScreen_to_createNewOrder)
    }

    fun navigateToViewOrders() {
        navController.navigate(R.id.action_homeScreen_to_viewOrdersFragment)
    }
}