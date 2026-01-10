package com.nitintraders.order.data

import android.os.Parcelable
import com.nitintraders.order.ui.adapter.EachBeltItemAdapter
import kotlinx.parcelize.Parcelize

@Parcelize
data class BeltItem(
    val beltType: EachBeltItemAdapter.BeltType,
    var size: Int? = null,
    var quantity: Int? = null,
    var totalInches: Int = 0,
) : Parcelable
