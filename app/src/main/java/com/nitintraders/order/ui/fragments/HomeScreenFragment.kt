package com.nitintraders.order.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.nitintraders.order.R
import com.nitintraders.order.databinding.FragmentHomeScreenBinding
import com.nitintraders.order.ui.activities.MainActivity
import com.nitintraders.order.viewmodel.MainViewModel
import kotlin.getValue

class HomeScreenFragment : Fragment() {

    private lateinit var binding: FragmentHomeScreenBinding
    private val mainViewModel: MainViewModel by activityViewModels()

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

        binding.btnLanguageEnglish.setOnClickListener {
            mainViewModel.setAppLanguage("en")
        }

        binding.btnLanguageGujarati.setOnClickListener {
            mainViewModel.setAppLanguage("gu")
        }
    }
}
