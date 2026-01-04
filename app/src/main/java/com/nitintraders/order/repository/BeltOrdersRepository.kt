package com.nitintraders.order.repository

import com.nitintraders.order.data.BeltOrder
import com.nitintraders.order.datasource.BeltOrdersDataSource
import javax.inject.Inject

interface BeltOrdersRepository {

    suspend fun addOrder(beltOrder: BeltOrder): Long

    suspend fun updateOrder(beltOrder: BeltOrder)

    suspend fun getAllOrders(): List<BeltOrder>

    suspend fun deleteOrder(beltOrder: BeltOrder): Boolean
}

class BeltOrdersRepositoryImpl @Inject constructor(
    private val beltOrdersDataSource: BeltOrdersDataSource,
) : BeltOrdersRepository {

    override suspend fun addOrder(beltOrder: BeltOrder): Long {
        return if (beltOrdersDataSource.orderExists(beltOrder.orderId)) {
            beltOrdersDataSource.updateOrder(beltOrder)
            beltOrder.orderId
        } else {
            beltOrdersDataSource.addNewOrder(beltOrder)
        }
    }

    override suspend fun updateOrder(beltOrder: BeltOrder) {
        beltOrdersDataSource.updateOrder(beltOrder)
    }

    override suspend fun getAllOrders(): List<BeltOrder> = beltOrdersDataSource.getAllOrders()

    override suspend fun deleteOrder(beltOrder: BeltOrder): Boolean =
        beltOrdersDataSource.deleteOrder(beltOrder)

}
