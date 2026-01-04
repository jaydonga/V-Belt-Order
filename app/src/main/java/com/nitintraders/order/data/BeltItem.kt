package com.nitintraders.order.data

import com.nitintraders.order.ui.adapter.EachBeltItemAdapter

data class BeltItem(
    val beltType: EachBeltItemAdapter.BeltType,
    var size: Int? = null,
    var quantity: Int? = null,
    var totalInches: Int = 0,
)
