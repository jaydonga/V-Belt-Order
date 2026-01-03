package com.nitintraders.v_beltorder.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nitintraders.v_beltorder.data.BeltItem
import com.nitintraders.v_beltorder.data.BeltOrder
import tools.jackson.core.type.TypeReference
import tools.jackson.module.kotlin.jacksonObjectMapper

@Entity
data class BeltOrderEntity(
    @PrimaryKey(autoGenerate = true) val orderId: Long = -1,
    val customerName: String,
    val totalBelts: Int,
    val grandTotal: Float,
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
            totalBelts = totalBelts,
            grandTotal = grandTotal,
            beltItems = getBeltItems(),
        )
    }

    companion object {
        const val TABLE_NAME = "BeltOrderEntity"
    }
}
