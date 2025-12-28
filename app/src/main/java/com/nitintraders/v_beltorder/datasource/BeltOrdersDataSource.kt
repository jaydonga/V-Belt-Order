package com.nitintraders.v_beltorder.datasource

import com.nitintraders.v_beltorder.data.BeltOrder

interface BeltOrdersDataSource {
    fun addNewOrder(beltOrder: BeltOrder)

    fun updateOrder(beltOrder: BeltOrder)
}

class BeltOrdersDataSourceImpl() : BeltOrdersDataSource {

    override fun addNewOrder(beltOrder: BeltOrder) {

    }

    override fun updateOrder(beltOrder: BeltOrder) {

    }

}
