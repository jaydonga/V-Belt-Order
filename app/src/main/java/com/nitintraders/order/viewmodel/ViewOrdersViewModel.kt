package com.nitintraders.order.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nitintraders.order.data.BeltOrder
import com.nitintraders.order.repository.BeltOrdersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewOrdersViewModel @Inject constructor(
    private val beltOrdersRepository: BeltOrdersRepository
) : ViewModel() {

    private val _allBeltOrders = MutableStateFlow<List<BeltOrder>>(emptyList())
    val allBeltOrders: Flow<List<BeltOrder>> = _allBeltOrders

    fun getAllBeltOrders() {
        viewModelScope.launch {
            val listBeltOrders = beltOrdersRepository.getAllOrders()
            _allBeltOrders.value = listBeltOrders
        }
    }

}
