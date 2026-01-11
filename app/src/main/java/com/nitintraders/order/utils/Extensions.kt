package com.nitintraders.order.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import kotlin.math.roundToInt

fun Int?.orZero(): Int = this ?: 0

fun Float?.orZero(): Float = this ?: 0F

fun Float.toMaxTwoDecimalPlaces(): Float {
    return (this * 100).roundToInt() / 100.0F
}

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "languageSettings")
