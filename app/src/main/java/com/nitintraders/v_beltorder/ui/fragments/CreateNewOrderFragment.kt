package com.nitintraders.v_beltorder.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.nitintraders.v_beltorder.R
import com.nitintraders.v_beltorder.databinding.FragmentCreateNewOrderBinding
import com.nitintraders.v_beltorder.ui.adapter.BeltItemAdapter
import com.nitintraders.v_beltorder.utils.orZero
import com.nitintraders.v_beltorder.utils.toMaxTwoDecimalPlaces

class CreateNewOrderFragment : Fragment() {

    private val numberOfBlankItems = 5
    private lateinit var binding: FragmentCreateNewOrderBinding
    private lateinit var adapterBeltTypeA: BeltItemAdapter
    private lateinit var adapterBeltTypeB: BeltItemAdapter
    private lateinit var adapterBeltTypeC: BeltItemAdapter

    private var totalPriceForBeltsA = 0f
    private var totalPriceForBeltsB = 0f
    private var totalPriceForBeltsC = 0f
    private var grandTotal = 0f

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

        handleClickListeners()

        addAdapterItemUpdateListener()

        binding.editTextCustomerName.addTextChangedListener { text ->
            if (text.isNullOrEmpty()) {
                binding.textInputLayoutCustomerName.error = getString(R.string.error_enter_customer_name)
            } else {
                binding.textInputLayoutCustomerName.isErrorEnabled = false
            }
        }

        binding.layoutBeltTypeA.editTextPriceOfSize.addTextChangedListener { text ->
            if (text.isNullOrEmpty() || text.toString().toFloat() <= 0F) {
                binding.layoutBeltTypeA.textInputLayoutPriceOf1Inch.error =
                    getString(R.string.error_enter_price_per_inch_a)
            } else {
                binding.layoutBeltTypeA.textInputLayoutPriceOf1Inch.isErrorEnabled = false
            }
            handleAdapterAItemUpdate()
        }

        binding.layoutBeltTypeB.editTextPriceOfSize.addTextChangedListener { text ->
            if (text.isNullOrEmpty() || text.toString().toFloat() <= 0F) {
                binding.layoutBeltTypeB.textInputLayoutPriceOf1Inch.error =
                    getString(R.string.error_enter_price_per_inch_b)
            } else {
                binding.layoutBeltTypeB.textInputLayoutPriceOf1Inch.isErrorEnabled = false
            }
            handleAdapterBItemUpdate()
        }

        binding.layoutBeltTypeC.editTextPriceOfSize.addTextChangedListener { text ->
            if (text.isNullOrEmpty() || text.toString().toFloat() <= 0F) {
                binding.layoutBeltTypeC.textInputLayoutPriceOf1Inch.error =
                    getString(R.string.error_enter_price_per_inch_c)
            } else {
                binding.layoutBeltTypeC.textInputLayoutPriceOf1Inch.isErrorEnabled = false
            }
            handleAdapterCItemUpdate()
        }

        binding.layoutBeltTypeA.btnAddNewItemSize.performClick()
        binding.layoutBeltTypeB.btnAddNewItemSize.performClick()
        binding.layoutBeltTypeC.btnAddNewItemSize.performClick()

        binding.editTextCustomerName.setText("")
        binding.layoutBeltTypeA.editTextPriceOfSize.setText("")
        binding.layoutBeltTypeB.editTextPriceOfSize.setText("")
        binding.layoutBeltTypeC.editTextPriceOfSize.setText("")
    }

    private fun addAdapterItemUpdateListener() {
        adapterBeltTypeA.setItemUpdateListener {
            handleAdapterAItemUpdate()
        }

        adapterBeltTypeB.setItemUpdateListener {
            handleAdapterBItemUpdate()
        }

        adapterBeltTypeC.setItemUpdateListener {
            handleAdapterCItemUpdate()
        }
    }

    private fun handleAdapterAItemUpdate() {
        val totalInchesForBeltTypeA = adapterBeltTypeA.allBeltItems.sumOf { it.totalInches }
        val pricePerInchForBeltTypeA =
            binding.layoutBeltTypeA.editTextPriceOfSize.text?.toString()?.toFloatOrNull().orZero()
        totalPriceForBeltsA = (totalInchesForBeltTypeA * pricePerInchForBeltTypeA).toMaxTwoDecimalPlaces()
        binding.layoutBeltTypeA.totalInchesCostForSize.text = getString(
            R.string.total_inches_and_price_a, totalInchesForBeltTypeA, totalPriceForBeltsA
        )

        grandTotal = (totalPriceForBeltsA + totalPriceForBeltsB + totalPriceForBeltsC).toMaxTwoDecimalPlaces()
        binding.textViewGrandTotal.text = getString(R.string.grand_total, grandTotal)
    }

    private fun handleAdapterBItemUpdate() {
        val totalInchesForBeltTypeB = adapterBeltTypeB.allBeltItems.sumOf { it.totalInches }
        val pricePerInchForBeltTypeB =
            binding.layoutBeltTypeB.editTextPriceOfSize.text?.toString()?.toFloatOrNull().orZero()
        totalPriceForBeltsB = (totalInchesForBeltTypeB * pricePerInchForBeltTypeB).toMaxTwoDecimalPlaces()
        binding.layoutBeltTypeB.totalInchesCostForSize.text = getString(
            R.string.total_inches_and_price_b, totalInchesForBeltTypeB, totalPriceForBeltsB
        )

        grandTotal = (totalPriceForBeltsA + totalPriceForBeltsB + totalPriceForBeltsC).toMaxTwoDecimalPlaces()
        binding.textViewGrandTotal.text = getString(R.string.grand_total, grandTotal)
    }

    private fun handleAdapterCItemUpdate() {
        val totalInchesForBeltTypeC = adapterBeltTypeC.allBeltItems.sumOf { it.totalInches }
        val pricePerInchForBeltTypeC =
            binding.layoutBeltTypeC.editTextPriceOfSize.text?.toString()?.toFloatOrNull().orZero()
        totalPriceForBeltsC = (totalInchesForBeltTypeC * pricePerInchForBeltTypeC).toMaxTwoDecimalPlaces()
        binding.layoutBeltTypeC.totalInchesCostForSize.text = getString(
            R.string.total_inches_and_price_c, totalInchesForBeltTypeC, totalPriceForBeltsC
        )

        grandTotal = (totalPriceForBeltsA + totalPriceForBeltsB + totalPriceForBeltsC).toMaxTwoDecimalPlaces()
        binding.textViewGrandTotal.text = getString(R.string.grand_total, grandTotal)
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
            adapterBeltTypeA.addNewItems(numberOfBlankItems)
        }
        binding.layoutBeltTypeB.btnAddNewItemSize.setOnClickListener {
            adapterBeltTypeB.addNewItems(numberOfBlankItems)
        }
        binding.layoutBeltTypeC.btnAddNewItemSize.setOnClickListener {
            adapterBeltTypeC.addNewItems(numberOfBlankItems)
        }
    }
}
