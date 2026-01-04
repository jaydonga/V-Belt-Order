package com.nitintraders.order.datasource

import com.nitintraders.order.data.BeltOrder
import com.nitintraders.order.database.BeltOrderDatabase
import javax.inject.Inject

interface BeltOrdersDataSource {
    fun addNewOrder(beltOrder: BeltOrder): Long

    fun updateOrder(beltOrder: BeltOrder)

    fun getAllOrders(): List<BeltOrder>

    fun deleteOrder(beltOrder: BeltOrder): Boolean
    fun orderExists(orderId: Long): Boolean
}

class BeltOrdersDataSourceImpl @Inject constructor(
    private val orderDatabase: BeltOrderDatabase
) : BeltOrdersDataSource {

    override fun addNewOrder(beltOrder: BeltOrder): Long {
        return orderDatabase.beltOrderDao().addNewOrder(beltOrder.toEntity())
    }

    override fun updateOrder(beltOrder: BeltOrder) {
        orderDatabase.beltOrderDao().updateOrder(beltOrder.toEntity())
    }

    override fun getAllOrders(): List<BeltOrder> {
        return orderDatabase.beltOrderDao().getAllOrders().map { it.toBeltOrder() }
    }

    override fun deleteOrder(beltOrder: BeltOrder): Boolean {
        return orderDatabase.beltOrderDao().deleteOrder(beltOrder.toEntity()) == 1
    }

    override fun orderExists(orderId: Long): Boolean {
        return orderDatabase.beltOrderDao().orderExists(orderId)
    }

}
