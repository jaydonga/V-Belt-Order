package com.nitintraders.order.viewmodel

import androidx.lifecycle.ViewModel
import com.nitintraders.order.data.BeltOrder
import com.nitintraders.order.repository.BeltOrdersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filter
import javax.inject.Inject

@HiltViewModel
class CreateNewOrderViewModel @Inject constructor(
    private val beltOrdersRepository: BeltOrdersRepository,
) : ViewModel() {

    private val _updatedOrderId = MutableStateFlow(-1L)
    val updatedOrderId: Flow<Long> = _updatedOrderId.filter { it > -1 }

    fun addOrder(beltOrder: BeltOrder) {
        val newOrderId = beltOrdersRepository.addOrder(beltOrder)
        _updatedOrderId.value = newOrderId
    }
}
