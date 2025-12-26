package com.nitintraders.v_beltorder.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.nitintraders.v_beltorder.R
import com.nitintraders.v_beltorder.databinding.FragmentCreateNewOrderBinding
import com.nitintraders.v_beltorder.ui.adapter.BeltItemAdapter

class CreateNewOrderFragment : Fragment() {

    private val numberOfBlankItems = 5
    private lateinit var binding: FragmentCreateNewOrderBinding
    private lateinit var adapterBeltTypeA: BeltItemAdapter
    private lateinit var adapterBeltTypeB: BeltItemAdapter
    private lateinit var adapterBeltTypeC: BeltItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapterBeltTypeA = BeltItemAdapter()
        adapterBeltTypeB = BeltItemAdapter()
        adapterBeltTypeC = BeltItemAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateNewOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.layoutBeltTypeA.textViewVBeltType.text = getString(R.string.label_v_belt_type_a)
        binding.layoutBeltTypeB.textViewVBeltType.text = getString(R.string.label_v_belt_type_b)
        binding.layoutBeltTypeC.textViewVBeltType.text = getString(R.string.label_v_belt_type_c)

        binding.layoutBeltTypeA.recyclerViewSizes.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.layoutBeltTypeB.recyclerViewSizes.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.layoutBeltTypeC.recyclerViewSizes.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )

        binding.layoutBeltTypeA.recyclerViewSizes.adapter = adapterBeltTypeA
        binding.layoutBeltTypeB.recyclerViewSizes.adapter = adapterBeltTypeB
        binding.layoutBeltTypeC.recyclerViewSizes.adapter = adapterBeltTypeC

        binding.layoutBeltTypeA.btnAddNewItemSize.performClick()
        binding.layoutBeltTypeB.btnAddNewItemSize.performClick()
        binding.layoutBeltTypeC.btnAddNewItemSize.performClick()

        handleClickListeners()
    }

    private fun handleClickListeners() {
        adapterBeltTypeA.setItemClickListener { beltItem, index ->
            Log.e(
                "CreateNewOrderFragment",
                "item to be removed from belt type A: $index, $beltItem"
            )
            adapterBeltTypeA.itemRemoved(index)
        }

        adapterBeltTypeB.setItemClickListener { beltItem, index ->
            Log.e(
                "CreateNewOrderFragment",
                "item to be removed from belt type B: $index, $beltItem"
            )
            adapterBeltTypeB.itemRemoved(index)
        }

        adapterBeltTypeC.setItemClickListener { beltItem, index ->
            Log.e(
                "CreateNewOrderFragment",
                "item to be removed from belt type C: $index, $beltItem"
            )
            adapterBeltTypeC.itemRemoved(index)
        }

        binding.layoutBeltTypeA.btnAddNewItemSize.setOnClickListener {
            adapterBeltTypeA.addBlankItems(numberOfBlankItems)
        }
        binding.layoutBeltTypeB.btnAddNewItemSize.setOnClickListener {
            adapterBeltTypeB.addBlankItems(numberOfBlankItems)
        }
        binding.layoutBeltTypeC.btnAddNewItemSize.setOnClickListener {
            adapterBeltTypeC.addBlankItems(numberOfBlankItems)
        }
    }
}
