package com.nitintraders.order.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.nitintraders.order.databinding.FragmentViewOrdersBinding
import com.nitintraders.order.ui.adapter.EachBeltOrderAdapter
import com.nitintraders.order.viewmodel.ViewOrdersViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ViewOrdersFragment : Fragment() {

    private lateinit var binding: FragmentViewOrdersBinding
    private val viewModel: ViewOrdersViewModel by viewModels()
    private lateinit var beltOrdersAdapter: EachBeltOrderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        beltOrdersAdapter = EachBeltOrderAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentViewOrdersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        beltOrdersAdapter.setItemClickListener { clickEventType, index ->
            when (clickEventType) {
                EachBeltOrderAdapter.ClickEventType.ItemRemoveEvent -> {
                    // Handle item remove event
                }

                EachBeltOrderAdapter.ClickEventType.ItemClickEvent -> {
                    // Handle item click event
                }
            }
        }

        binding.recyclerViewAllOrder.adapter = beltOrdersAdapter
        binding.recyclerViewAllOrder.layoutManager = LinearLayoutManager(requireContext())

        lifecycleScope.launch {
            viewModel.allBeltOrders.flowWithLifecycle(lifecycle, Lifecycle.State.CREATED).collect {
                beltOrdersAdapter.setBeltOrders(it)
            }
        }
    }
}
