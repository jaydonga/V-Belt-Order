package com.nitintraders.order.utils

import kotlin.math.roundToInt

fun Int?.orZero(): Int = this ?: 0

fun Float?.orZero(): Float = this ?: 0F

fun Float.toMaxTwoDecimalPlaces(): Float {
    return (this * 100).roundToInt() / 100.0F
}