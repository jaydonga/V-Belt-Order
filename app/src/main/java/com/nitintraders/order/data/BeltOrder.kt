package com.nitintraders.order.data

import android.os.Parcelable
import com.nitintraders.order.database.BeltOrderEntity
import kotlinx.parcelize.Parcelize
import tools.jackson.databind.ObjectMapper

@Parcelize
data class BeltOrder(
    val orderId: Long,
    val customerName: String,
    val totalBeltsOfTypeA: Int,
    val totalBeltsOfTypeB: Int,
    val totalBeltsOfTypeC: Int,
    val totalBelts: Int,
    val grandTotal: Float,
    val orderDateTime: Long = System.currentTimeMillis(),
    val beltItems: List<BeltItem>,
) : Parcelable {
    fun toEntity(): BeltOrderEntity {
        return BeltOrderEntity(
            orderId = if (orderId == -1L) 0 else orderId,
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
