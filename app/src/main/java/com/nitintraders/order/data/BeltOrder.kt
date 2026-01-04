package com.nitintraders.order.data

import com.nitintraders.order.database.BeltOrderEntity
import tools.jackson.databind.ObjectMapper

data class BeltOrder(
    val orderId: Long = -1,
    val customerName: String,
    val totalBeltsOfTypeA: Int,
    val totalBeltsOfTypeB: Int,
    val totalBeltsOfTypeC: Int,
    val totalBelts: Int,
    val grandTotal: Float,
    val orderDateTime: Long = System.currentTimeMillis(),
    val beltItems: List<BeltItem>,
) {
    fun toEntity(): BeltOrderEntity {
        return BeltOrderEntity(
            orderId = orderId,
            customerName = customerName,
            totalBeltsOfTypeA = totalBeltsOfTypeA,
            totalBeltsOfTypeB = totalBeltsOfTypeB,
            totalBeltsOfTypeC = totalBeltsOfTypeC,
            totalBelts = totalBelts,
            grandTotal = grandTotal,
            orderDateTime = orderDateTime,
            beltItems = ObjectMapper().writeValueAsString(beltItems),
        )
    }
}
