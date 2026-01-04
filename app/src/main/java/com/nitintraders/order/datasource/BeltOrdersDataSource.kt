package com.nitintraders.order.datasource

import com.nitintraders.order.data.BeltOrder
import com.nitintraders.order.database.BeltOrderDatabase
import javax.inject.Inject

interface BeltOrdersDataSource {
    suspend fun addNewOrder(beltOrder: BeltOrder): Long

    suspend fun updateOrder(beltOrder: BeltOrder)

    suspend fun getAllOrders(): List<BeltOrder>

    suspend fun deleteOrder(beltOrder: BeltOrder): Boolean
    suspend fun orderExists(orderId: Long): Boolean
}

class BeltOrdersDataSourceImpl @Inject constructor(
    private val orderDatabase: BeltOrderDatabase
) : BeltOrdersDataSource {

    override suspend fun addNewOrder(beltOrder: BeltOrder): Long {
        return orderDatabase.beltOrderDao().addNewOrder(beltOrder.toEntity())
    }

    override suspend fun updateOrder(beltOrder: BeltOrder) {
        orderDatabase.beltOrderDao().updateOrder(beltOrder.toEntity())
    }

    override suspend fun getAllOrders(): List<BeltOrder> {
        return orderDatabase.beltOrderDao().getAllOrders().map { it.toBeltOrder() }
    }

    override suspend fun deleteOrder(beltOrder: BeltOrder): Boolean {
        return orderDatabase.beltOrderDao().deleteOrder(beltOrder.toEntity()) == 1
    }

    override suspend fun orderExists(orderId: Long): Boolean {
        return orderDatabase.beltOrderDao().orderExists(orderId)
    }

}
