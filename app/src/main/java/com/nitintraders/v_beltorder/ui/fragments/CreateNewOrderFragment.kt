package com.nitintraders.v_beltorder.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.nitintraders.v_beltorder.R
import com.nitintraders.v_beltorder.databinding.FragmentCreateNewOrderBinding
import com.nitintraders.v_beltorder.databinding.IncludeCreateOrderForBeltTypeBinding
import com.nitintraders.v_beltorder.ui.adapter.BeltItemAdapter
import com.nitintraders.v_beltorder.utils.orZero
import com.nitintraders.v_beltorder.utils.toMaxTwoDecimalPlaces
import com.nitintraders.v_beltorder.viewmodel.CreateNewOrderViewModel


class CreateNewOrderFragment : Fragment() {

    private val numberOfBlankItems = 5
    private lateinit var binding: FragmentCreateNewOrderBinding
    private lateinit var adapterBeltTypeA: BeltItemAdapter
    private lateinit var adapterBeltTypeB: BeltItemAdapter
    private lateinit var adapterBeltTypeC: BeltItemAdapter
    private var totalBeltsOfTypeA = 0
    private var totalBeltsOfTypeB = 0
    private var totalBeltsOfTypeC = 0
    private var totalBelts = 0

    private var totalPriceForBeltsA = 0f
    private var totalPriceForBeltsB = 0f
    private var totalPriceForBeltsC = 0f

    private var grandTotal = 0f

    private lateinit var viewModel: CreateNewOrderViewModel
    private var customerName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapterBeltTypeA = BeltItemAdapter()
        adapterBeltTypeB = BeltItemAdapter()
        adapterBeltTypeC = BeltItemAdapter()

        viewModel = ViewModelProvider(requireActivity()).get(CreateNewOrderViewModel::class.java)
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
                binding.layoutBeltTypeA.root.isVisible = false
                binding.layoutBeltTypeB.root.isVisible = false
                binding.layoutBeltTypeC.root.isVisible = false
            } else {
                customerName = text.toString()
                binding.textInputLayoutCustomerName.isErrorEnabled = false
                binding.layoutBeltTypeA.root.isVisible = true
                binding.layoutBeltTypeB.root.isVisible = true
                binding.layoutBeltTypeC.root.isVisible = true
            }
        }

        binding.layoutBeltTypeA.editTextPriceOfSize.addTextChangedListener { text ->
            if (text.isNullOrEmpty() || text.toString().toFloat() <= 0F) {
                binding.layoutBeltTypeA.textInputLayoutPriceOf1Inch.error =
                    getString(R.string.error_enter_price_per_inch_a)
                setBeltItemInputVisibility(false, binding.layoutBeltTypeA)
            } else {
                binding.layoutBeltTypeA.textInputLayoutPriceOf1Inch.isErrorEnabled = false
                setBeltItemInputVisibility(true, binding.layoutBeltTypeA)
            }
            handleAdapterAItemUpdate()
        }

        binding.layoutBeltTypeB.editTextPriceOfSize.addTextChangedListener { text ->
            if (text.isNullOrEmpty() || text.toString().toFloat() <= 0F) {
                binding.layoutBeltTypeB.textInputLayoutPriceOf1Inch.error =
                    getString(R.string.error_enter_price_per_inch_b)
                setBeltItemInputVisibility(false, binding.layoutBeltTypeB)
            } else {
                binding.layoutBeltTypeB.textInputLayoutPriceOf1Inch.isErrorEnabled = false
                setBeltItemInputVisibility(true, binding.layoutBeltTypeB)
            }
            handleAdapterBItemUpdate()
        }

        binding.layoutBeltTypeC.editTextPriceOfSize.addTextChangedListener { text ->
            if (text.isNullOrEmpty() || text.toString().toFloat() <= 0F) {
                binding.layoutBeltTypeC.textInputLayoutPriceOf1Inch.error =
                    getString(R.string.error_enter_price_per_inch_c)
                setBeltItemInputVisibility(false, binding.layoutBeltTypeC)
            } else {
                binding.layoutBeltTypeC.textInputLayoutPriceOf1Inch.isErrorEnabled = false
                setBeltItemInputVisibility(true, binding.layoutBeltTypeC)
            }
            handleAdapterCItemUpdate()
        }

        binding.layoutBeltTypeA.buttonAddNewItemSize.performClick()
        binding.layoutBeltTypeB.buttonAddNewItemSize.performClick()
        binding.layoutBeltTypeC.buttonAddNewItemSize.performClick()

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
        totalBeltsOfTypeA = adapterBeltTypeA.allBeltItems.sumOf { it.quantity.orZero() }
        val pricePerInchForBeltTypeA =
            binding.layoutBeltTypeA.editTextPriceOfSize.text?.toString()?.toFloatOrNull().orZero()
        totalPriceForBeltsA = (totalInchesForBeltTypeA * pricePerInchForBeltTypeA).toMaxTwoDecimalPlaces()
        binding.layoutBeltTypeA.textViewTotalInchesCostForSize.text = getString(
            R.string.total_of_type_a, totalBeltsOfTypeA, totalInchesForBeltTypeA, totalPriceForBeltsA
        )

        grandTotal = (totalPriceForBeltsA + totalPriceForBeltsB + totalPriceForBeltsC).toMaxTwoDecimalPlaces()
        totalBelts = totalBeltsOfTypeA + totalBeltsOfTypeB + totalBeltsOfTypeC
        binding.textViewGrandTotal.text = getString(R.string.grand_total, totalBelts, grandTotal)
    }

    private fun handleAdapterBItemUpdate() {
        val totalInchesForBeltTypeB = adapterBeltTypeB.allBeltItems.sumOf { it.totalInches }
        totalBeltsOfTypeB = adapterBeltTypeB.allBeltItems.sumOf { it.quantity.orZero() }
        val pricePerInchForBeltTypeB =
            binding.layoutBeltTypeB.editTextPriceOfSize.text?.toString()?.toFloatOrNull().orZero()
        totalPriceForBeltsB = (totalInchesForBeltTypeB * pricePerInchForBeltTypeB).toMaxTwoDecimalPlaces()
        binding.layoutBeltTypeB.textViewTotalInchesCostForSize.text = getString(
            R.string.total_of_type_b, totalBeltsOfTypeB, totalInchesForBeltTypeB, totalPriceForBeltsB
        )

        grandTotal = (totalPriceForBeltsA + totalPriceForBeltsB + totalPriceForBeltsC).toMaxTwoDecimalPlaces()
        totalBelts = totalBeltsOfTypeA + totalBeltsOfTypeB + totalBeltsOfTypeC
        binding.textViewGrandTotal.text = getString(R.string.grand_total, totalBelts, grandTotal)
    }

    private fun handleAdapterCItemUpdate() {
        val totalInchesForBeltTypeC = adapterBeltTypeC.allBeltItems.sumOf { it.totalInches }
        totalBeltsOfTypeC = adapterBeltTypeC.allBeltItems.sumOf { it.quantity.orZero() }
        val pricePerInchForBeltTypeC =
            binding.layoutBeltTypeC.editTextPriceOfSize.text?.toString()?.toFloatOrNull().orZero()
        totalPriceForBeltsC = (totalInchesForBeltTypeC * pricePerInchForBeltTypeC).toMaxTwoDecimalPlaces()
        binding.layoutBeltTypeC.textViewTotalInchesCostForSize.text = getString(
            R.string.total_of_type_c, totalBeltsOfTypeC, totalInchesForBeltTypeC, totalPriceForBeltsC
        )

        grandTotal = (totalPriceForBeltsA + totalPriceForBeltsB + totalPriceForBeltsC).toMaxTwoDecimalPlaces()
        totalBelts = totalBeltsOfTypeA + totalBeltsOfTypeB + totalBeltsOfTypeC
        binding.textViewGrandTotal.text = getString(R.string.grand_total, totalBelts, grandTotal)
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

        binding.layoutBeltTypeA.buttonAddNewItemSize.setOnClickListener {
            adapterBeltTypeA.addNewItems(numberOfBlankItems)
        }
        binding.layoutBeltTypeB.buttonAddNewItemSize.setOnClickListener {
            adapterBeltTypeB.addNewItems(numberOfBlankItems)
        }
        binding.layoutBeltTypeC.buttonAddNewItemSize.setOnClickListener {
            adapterBeltTypeC.addNewItems(numberOfBlankItems)
        }
    }

    private fun setBeltItemInputVisibility(
        visible: Boolean,
        layoutBeltType: IncludeCreateOrderForBeltTypeBinding,
    ) {
        layoutBeltType.recyclerViewSizes.isVisible = visible
        layoutBeltType.dividerCreateOrderForBeltType.isVisible = visible
        layoutBeltType.textViewTotalInchesCostForSize.isVisible = visible
        layoutBeltType.buttonAddNewItemSize.isVisible = visible
    }

    override fun onStop() {
        super.onStop()
        Log.e("CreateNewOrderFragment", "onStop() called")
        val allBeltItems = listOf(
            adapterBeltTypeA.allBeltItems.filter { it.totalInches > 0 },
            adapterBeltTypeB.allBeltItems.filter { it.totalInches > 0 },
            adapterBeltTypeC.allBeltItems.filter { it.totalInches > 0 },
        ).flatten()
        viewModel.saveNewOrder(
            customerName,
            totalBelts,
            grandTotal,
            allBeltItems,
        )
    }
}
