package com.nitintraders.v_beltorder.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.nitintraders.v_beltorder.data.BeltOrder
import com.nitintraders.v_beltorder.repository.BeltOrdersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreateNewOrderViewModel @Inject constructor(
    private val beltOrdersRepository: BeltOrdersRepository,
) : ViewModel() {

    fun addNewOrder(beltOrder: BeltOrder) {
        Log.d("CreateNewOrderViewModel", "Adding new order $beltOrder")
        beltOrdersRepository.addNewOrder(beltOrder)
    }
}
