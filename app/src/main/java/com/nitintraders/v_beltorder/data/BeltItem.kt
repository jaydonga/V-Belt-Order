package com.nitintraders.v_beltorder.data

import com.nitintraders.v_beltorder.ui.adapter.EachBeltItemAdapter

data class BeltItem(
    val beltType: EachBeltItemAdapter.BeltType,
    var size: Int? = null,
    var quantity: Int? = null,
    var totalInches: Int = 0,
)
