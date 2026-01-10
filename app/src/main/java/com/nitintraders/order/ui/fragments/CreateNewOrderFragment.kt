package com.nitintraders.order.ui.fragments

import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import com.nitintraders.order.R
import com.nitintraders.order.data.BeltItem
import com.nitintraders.order.data.BeltOrder
import com.nitintraders.order.databinding.FragmentCreateNewOrderBinding
import com.nitintraders.order.databinding.IncludeCreateOrderForBeltTypeBinding
import com.nitintraders.order.ui.activities.MainActivity
import com.nitintraders.order.ui.adapter.EachBeltItemAdapter
import com.nitintraders.order.ui.adapter.EachBeltItemAdapter.BeltType.A
import com.nitintraders.order.ui.adapter.EachBeltItemAdapter.BeltType.B
import com.nitintraders.order.ui.adapter.EachBeltItemAdapter.BeltType.C
import com.nitintraders.order.utils.orZero
import com.nitintraders.order.utils.toMaxTwoDecimalPlaces
import com.nitintraders.order.viewmodel.CreateNewOrderViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CreateNewOrderFragment : Fragment() {

    private lateinit var binding: FragmentCreateNewOrderBinding
    private lateinit var adapterBeltTypeA: EachBeltItemAdapter
    private lateinit var adapterBeltTypeB: EachBeltItemAdapter
    private lateinit var adapterBeltTypeC: EachBeltItemAdapter
    private var totalBeltsOfTypeA = 0
    private var totalBeltsOfTypeB = 0
    private var totalBeltsOfTypeC = 0
    private var totalBelts = 0

    private var totalPriceForBeltsA = 0f
    private var totalPriceForBeltsB = 0f
    private var totalPriceForBeltsC = 0f

    private var grandTotal = 0f

    private val viewModel: CreateNewOrderViewModel by viewModels()

    private var customerName = ""

    private var orderId = -1L
    private var orderSavedTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapterBeltTypeA = EachBeltItemAdapter(beltType = A)
        adapterBeltTypeB = EachBeltItemAdapter(beltType = B)
        adapterBeltTypeC = EachBeltItemAdapter(beltType = C)
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

        binding.layoutBeltTypeA.textViewVBeltType.text = getText(R.string.label_v_belt_type_a)
        binding.layoutBeltTypeB.textViewVBeltType.text = getText(R.string.label_v_belt_type_b)
        binding.layoutBeltTypeC.textViewVBeltType.text = getText(R.string.label_v_belt_type_c)

        binding.layoutBeltTypeA.recyclerViewSizes.layoutManager = getLinearLayoutManager()
        binding.layoutBeltTypeB.recyclerViewSizes.layoutManager = getLinearLayoutManager()
        binding.layoutBeltTypeC.recyclerViewSizes.layoutManager = getLinearLayoutManager()

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

        binding.layoutBeltTypeA.editTextPriceOfBeltType.addTextChangedListener { text ->
            if (text.isNullOrEmpty() || text.toString().toFloat() <= 0F) {
                binding.layoutBeltTypeA.textInputLayoutPriceOf1Inch.error =
                    getText(R.string.error_enter_price_per_inch_a)
                setBeltItemInputVisibility(false, binding.layoutBeltTypeA)
            } else {
                binding.layoutBeltTypeA.textInputLayoutPriceOf1Inch.isErrorEnabled = false
                setBeltItemInputVisibility(true, binding.layoutBeltTypeA)
            }
            handleAdapterAItemUpdate()
        }

        binding.layoutBeltTypeB.editTextPriceOfBeltType.addTextChangedListener { text ->
            if (text.isNullOrEmpty() || text.toString().toFloat() <= 0F) {
                binding.layoutBeltTypeB.textInputLayoutPriceOf1Inch.error =
                    getText(R.string.error_enter_price_per_inch_b)
                setBeltItemInputVisibility(false, binding.layoutBeltTypeB)
            } else {
                binding.layoutBeltTypeB.textInputLayoutPriceOf1Inch.isErrorEnabled = false
                setBeltItemInputVisibility(true, binding.layoutBeltTypeB)
            }
            handleAdapterBItemUpdate()
        }

        binding.layoutBeltTypeC.editTextPriceOfBeltType.addTextChangedListener { text ->
            if (text.isNullOrEmpty() || text.toString().toFloat() <= 0F) {
                binding.layoutBeltTypeC.textInputLayoutPriceOf1Inch.error =
                    getText(R.string.error_enter_price_per_inch_c)
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
        binding.layoutBeltTypeA.editTextPriceOfBeltType.setText("")
        binding.layoutBeltTypeB.editTextPriceOfBeltType.setText("")
        binding.layoutBeltTypeC.editTextPriceOfBeltType.setText("")
    }

    private fun getLinearLayoutManager() = LinearLayoutManager(requireContext(), VERTICAL, false)

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
            binding.layoutBeltTypeA.editTextPriceOfBeltType.text?.toString()?.toFloatOrNull().orZero()
        totalPriceForBeltsA = (totalInchesForBeltTypeA * pricePerInchForBeltTypeA).toMaxTwoDecimalPlaces()
        val totalOfTypeA = getString(
            R.string.total_of_type_a, totalBeltsOfTypeA, totalInchesForBeltTypeA, totalPriceForBeltsA
        )
        binding.layoutBeltTypeA.textViewTotalInchesCostForSize.text = Html.fromHtml(
            totalOfTypeA,
            Html.FROM_HTML_MODE_COMPACT
        )

        grandTotal = (totalPriceForBeltsA + totalPriceForBeltsB + totalPriceForBeltsC).toMaxTwoDecimalPlaces()
        totalBelts = totalBeltsOfTypeA + totalBeltsOfTypeB + totalBeltsOfTypeC
        binding.textViewGrandTotal.text =
            getString(R.string.total_belts_and_grand_total, totalBelts, grandTotal)
    }

    private fun handleAdapterBItemUpdate() {
        val totalInchesForBeltTypeB = adapterBeltTypeB.allBeltItems.sumOf { it.totalInches }
        totalBeltsOfTypeB = adapterBeltTypeB.allBeltItems.sumOf { it.quantity.orZero() }
        val pricePerInchForBeltTypeB =
            binding.layoutBeltTypeB.editTextPriceOfBeltType.text?.toString()?.toFloatOrNull().orZero()
        totalPriceForBeltsB = (totalInchesForBeltTypeB * pricePerInchForBeltTypeB).toMaxTwoDecimalPlaces()
        val totalOfTypeB = getString(
            R.string.total_of_type_b, totalBeltsOfTypeB, totalInchesForBeltTypeB, totalPriceForBeltsB
        )
        binding.layoutBeltTypeB.textViewTotalInchesCostForSize.text = Html.fromHtml(
            totalOfTypeB,
            Html.FROM_HTML_MODE_COMPACT
        )

        grandTotal = (totalPriceForBeltsA + totalPriceForBeltsB + totalPriceForBeltsC).toMaxTwoDecimalPlaces()
        totalBelts = totalBeltsOfTypeA + totalBeltsOfTypeB + totalBeltsOfTypeC
        binding.textViewGrandTotal.text =
            getString(R.string.total_belts_and_grand_total, totalBelts, grandTotal)
    }

    private fun handleAdapterCItemUpdate() {
        val totalInchesForBeltTypeC = adapterBeltTypeC.allBeltItems.sumOf { it.totalInches }
        totalBeltsOfTypeC = adapterBeltTypeC.allBeltItems.sumOf { it.quantity.orZero() }
        val pricePerInchForBeltTypeC =
            binding.layoutBeltTypeC.editTextPriceOfBeltType.text?.toString()?.toFloatOrNull().orZero()
        totalPriceForBeltsC = (totalInchesForBeltTypeC * pricePerInchForBeltTypeC).toMaxTwoDecimalPlaces()
        val totalOfTypeC = getString(
            R.string.total_of_type_c, totalBeltsOfTypeC, totalInchesForBeltTypeC, totalPriceForBeltsC
        )
        binding.layoutBeltTypeC.textViewTotalInchesCostForSize.text = Html.fromHtml(
            totalOfTypeC,
            Html.FROM_HTML_MODE_COMPACT
        )

        grandTotal = (totalPriceForBeltsA + totalPriceForBeltsB + totalPriceForBeltsC).toMaxTwoDecimalPlaces()
        totalBelts = totalBeltsOfTypeA + totalBeltsOfTypeB + totalBeltsOfTypeC
        binding.textViewGrandTotal.text =
            getString(R.string.total_belts_and_grand_total, totalBelts, grandTotal)
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
            adapterBeltTypeA.addNewItems(NUMBER_OF_BLANK_ITEMS)
        }
        binding.layoutBeltTypeB.buttonAddNewItemSize.setOnClickListener {
            adapterBeltTypeB.addNewItems(NUMBER_OF_BLANK_ITEMS)
        }
        binding.layoutBeltTypeC.buttonAddNewItemSize.setOnClickListener {
            adapterBeltTypeC.addNewItems(NUMBER_OF_BLANK_ITEMS)
        }

        binding.buttonSave.setOnClickListener {
            val allBeltItems = listOf(
                adapterBeltTypeA.allBeltItems.filter { it.totalInches > 0 },
                adapterBeltTypeB.allBeltItems.filter { it.totalInches > 0 },
                adapterBeltTypeC.allBeltItems.filter { it.totalInches > 0 },
            ).flatten()
            if (customerName.isNotEmpty() && allBeltItems.isNotEmpty()) {
                saveOrder(allBeltItems)
                orderSavedTime = System.currentTimeMillis()
            }
        }

        lifecycleScope.launch {
            viewModel
                .updatedOrderId
                .flowWithLifecycle(lifecycle, Lifecycle.State.CREATED)
                .filter { it >= 0L }
                .collect {
                    orderId = it
                    (activity as MainActivity).navigateToViewOrders()
                }
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

    private fun saveOrder(allBeltItems: List<BeltItem>) {
        val beltOrder = BeltOrder(
            orderId = if (orderId == -1L) 0 else orderId,
            customerName = customerName,
            totalBeltsOfTypeA = totalBeltsOfTypeA,
            totalBeltsOfTypeB = totalBeltsOfTypeB,
            totalBeltsOfTypeC = totalBeltsOfTypeC,
            totalBelts = totalBelts,
            grandTotal = grandTotal,
            beltItems = allBeltItems,
        )
        viewModel.addOrder(beltOrder)
    }

    private companion object {
        private const val NUMBER_OF_BLANK_ITEMS = 5
    }
}
