package com.nitintraders.order.repository

import com.nitintraders.order.data.BeltOrder
import com.nitintraders.order.datasource.BeltOrdersDataSource
import javax.inject.Inject

interface BeltOrdersRepository {

    fun addOrder(beltOrder: BeltOrder): Long

    fun updateOrder(beltOrder: BeltOrder)

    fun getAllOrders(): List<BeltOrder>

    fun deleteOrder(beltOrder: BeltOrder): Boolean
}

class BeltOrdersRepositoryImpl @Inject constructor(
    private val beltOrdersDataSource: BeltOrdersDataSource,
) : BeltOrdersRepository {

    override fun addOrder(beltOrder: BeltOrder): Long {
        return if (beltOrdersDataSource.orderExists(beltOrder.orderId)) {
            beltOrdersDataSource.updateOrder(beltOrder)
            beltOrder.orderId
        } else {
            beltOrdersDataSource.addNewOrder(beltOrder)
        }
    }

    override fun updateOrder(beltOrder: BeltOrder) {
        beltOrdersDataSource.updateOrder(beltOrder)
    }

    override fun getAllOrders(): List<BeltOrder> = beltOrdersDataSource.getAllOrders()

    override fun deleteOrder(beltOrder: BeltOrder): Boolean = beltOrdersDataSource.deleteOrder(beltOrder)

}
