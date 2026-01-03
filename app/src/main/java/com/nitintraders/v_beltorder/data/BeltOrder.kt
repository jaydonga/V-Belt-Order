package com.nitintraders.v_beltorder.data

import com.nitintraders.v_beltorder.database.BeltOrderEntity
import tools.jackson.databind.ObjectMapper

data class BeltOrder(
    val orderId: Long = -1,
    val customerName: String,
    val totalBelts: Int,
    val grandTotal: Float,
    val beltItems: List<BeltItem>,
) {
    fun toEntity(): BeltOrderEntity {
        return BeltOrderEntity(
            orderId = orderId,
            customerName = customerName,
            totalBelts = totalBelts,
            grandTotal = grandTotal,
            beltItems = ObjectMapper().writeValueAsString(beltItems),
        )
    }
}
