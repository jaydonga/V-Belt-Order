package com.nitintraders.order.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nitintraders.order.data.BeltOrder
import com.nitintraders.order.repository.BeltOrdersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewOrdersViewModel @Inject constructor(
    private val beltOrdersRepository: BeltOrdersRepository
) : ViewModel() {

    private val _allBeltOrders = MutableStateFlow<List<BeltOrder>>(emptyList())
    val allBeltOrders: Flow<List<BeltOrder>> = _allBeltOrders

    private val _orderDeleted = MutableStateFlow<BeltOrder?>(null)
    val orderDeleted: Flow<BeltOrder> = _orderDeleted.filterNotNull()

    fun retrieveAllBeltOrders() {
        viewModelScope.launch {
            val listBeltOrders = beltOrdersRepository.getAllOrders()
            _allBeltOrders.value = listBeltOrders
        }
    }

    fun deleteOrder(beltOrder: BeltOrder) {
        viewModelScope.launch {
            val deletionResult = beltOrdersRepository.deleteOrder(beltOrder)
            if (deletionResult) {
                _orderDeleted.value = beltOrder
            }
        }
    }
}
