package com.nitintraders.order.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nitintraders.order.data.BeltItem
import com.nitintraders.order.data.BeltOrder
import tools.jackson.core.type.TypeReference
import tools.jackson.module.kotlin.jacksonObjectMapper

@Entity
data class BeltOrderEntity(
    @PrimaryKey(autoGenerate = true) val orderId: Long,
    val customerName: String,
    val priceOfBeltTypeA: Float,
    val priceOfBeltTypeB: Float,
    val priceOfBeltTypeC: Float,
    val totalBeltsOfTypeA: Int,
    val totalBeltsOfTypeB: Int,
    val totalBeltsOfTypeC: Int,
    val totalBelts: Int,
    val grandTotal: Float,
    val orderDateTime: Long,
    val beltItems: String,
) {
    private fun getBeltItems(): List<BeltItem> = try {
        jacksonObjectMapper().readValue(beltItems, object : TypeReference<MutableList<BeltItem>>() {})
    } catch (e: Exception) {
        e.printStackTrace()
        emptyList()
    }

    fun toBeltOrder(): BeltOrder {
        return BeltOrder(
            orderId = orderId,
            customerName = customerName,
            priceOfBeltTypeA = priceOfBeltTypeA,
            priceOfBeltTypeB = priceOfBeltTypeB,
            priceOfBeltTypeC = priceOfBeltTypeC,
            totalBeltsOfTypeA = totalBeltsOfTypeA,
            totalBeltsOfTypeB = totalBeltsOfTypeB,
            totalBeltsOfTypeC = totalBeltsOfTypeC,
            totalBelts = totalBelts,
            grandTotal = grandTotal,
            orderDateTime = orderDateTime,
            beltItems = getBeltItems(),
        )
    }

    companion object {
        const val TABLE_NAME = "BeltOrderEntity"
    }
}
