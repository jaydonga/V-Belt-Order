package com.nitintraders.v_beltorder.repository

import com.nitintraders.v_beltorder.data.BeltOrder
import com.nitintraders.v_beltorder.datasource.BeltOrdersDataSource
import javax.inject.Inject

interface BeltOrdersRepository {

    fun addNewOrder(beltOrder: BeltOrder)

    fun updateOrder(beltOrder: BeltOrder)
}

class BeltOrdersRepositoryImpl @Inject constructor(
    private val beltOrdersDataSource: BeltOrdersDataSource,
) : BeltOrdersRepository {
    override fun addNewOrder(beltOrder: BeltOrder) {
        beltOrdersDataSource.addNewOrder(beltOrder)
    }

    override fun updateOrder(beltOrder: BeltOrder) {
        beltOrdersDataSource.updateOrder(beltOrder)
    }

}
