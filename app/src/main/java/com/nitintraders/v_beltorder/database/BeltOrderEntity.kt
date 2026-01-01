package com.nitintraders.v_beltorder.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BeltOrderEntity(
    @PrimaryKey val orderId: Int = -1,
    val customerName: String,
    val totalBelts: Int,
    val grandTotal: Float,
    val beltItems: String,
)
