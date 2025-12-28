package com.nitintraders.v_beltorder.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.nitintraders.v_beltorder.data.BeltItem

class CreateNewOrderViewModel : ViewModel() {

    fun saveNewOrder(
        customerName: String,
        totalBelts: Int,
        grandTotal: Float,
        beltItems: List<BeltItem>,
    ) {
        Log.d(
            "CreateNewOrderViewModel",
            "Saving new order for customer: $customerName, " +
                    "total belts: $totalBelts, grand total: $grandTotal, " +
                    "belt items: $beltItems"
        )
    }
}