package com.nitintraders.v_beltorder.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nitintraders.v_beltorder.R
import com.nitintraders.v_beltorder.databinding.FragmentCreateNewOrderBinding
import com.nitintraders.v_beltorder.ui.adapter.BeltItemAdapter

class CreateNewOrderFragment : Fragment() {

    private val blankItems = 5
    private lateinit var binding: FragmentCreateNewOrderBinding

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

        val adapterBeltTypeA = BeltItemAdapter(emptyList())
        val adapterBeltTypeB = BeltItemAdapter(emptyList())
        val adapterBeltTypeC = BeltItemAdapter(emptyList())

        binding.layoutBeltTypeA.btnAddNewItemSize.setOnClickListener {
            adapterBeltTypeA.addBlankItems(blankItems)
        }
        binding.layoutBeltTypeB.btnAddNewItemSize.setOnClickListener {
            adapterBeltTypeB.addBlankItems(blankItems)
        }
        binding.layoutBeltTypeC.btnAddNewItemSize.setOnClickListener {
            adapterBeltTypeC.addBlankItems(blankItems)
        }

        binding.layoutBeltTypeA.recyclerViewSizes.adapter = adapterBeltTypeA
        binding.layoutBeltTypeB.recyclerViewSizes.adapter = adapterBeltTypeB
        binding.layoutBeltTypeC.recyclerViewSizes.adapter = adapterBeltTypeC


    }
}
