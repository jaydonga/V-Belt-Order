package com.nitintraders.v_beltorder.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nitintraders.v_beltorder.data.BeltOrder
import com.nitintraders.v_beltorder.repository.BeltOrdersRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ViewOrdersViewModel(private val beltOrdersRepository: BeltOrdersRepository) : ViewModel() {

    private val _allBeltOrders = MutableStateFlow<List<BeltOrder>>(emptyList())
    val allBeltOrders: Flow<List<BeltOrder>> = _allBeltOrders

    fun getAllBeltOrders() {
        viewModelScope.launch {
            val listBeltOrders = beltOrdersRepository.getAllOrders()
            _allBeltOrders.value = listBeltOrders
        }
    }

}
