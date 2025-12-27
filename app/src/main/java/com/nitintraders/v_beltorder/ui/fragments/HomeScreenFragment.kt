package com.nitintraders.v_beltorder.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nitintraders.v_beltorder.R
import com.nitintraders.v_beltorder.databinding.FragmentHomeScreenBinding
import com.nitintraders.v_beltorder.ui.activities.MainActivity

class HomeScreenFragment : Fragment() {

    private lateinit var binding: FragmentHomeScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as MainActivity).supportActionBar?.title = getString(R.string.nitin_traders)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCreateNewOrder.setOnClickListener {
            (activity as MainActivity).navigateToCreateNewOrder()
        }

        binding.btnViewOrders.setOnClickListener {
            (activity as MainActivity).navigateToViewOrders()
        }
    }
}
