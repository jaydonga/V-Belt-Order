package com.nitintraders.v_beltorder.data

data class BeltOrder(
    val orderId: Int = -1,
    val customerName: String,
    val totalBelts: Int,
    val grandTotal: Float,
    val beltItems: List<BeltItem>,
)
