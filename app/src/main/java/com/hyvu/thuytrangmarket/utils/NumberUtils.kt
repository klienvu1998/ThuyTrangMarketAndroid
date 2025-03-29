package com.hyvu.thuytrangmarket.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

object NumberUtils {
    fun formatPrice(number: Double): String {
        val decimalFormat = DecimalFormat("#,###", DecimalFormatSymbols(Locale.getDefault()))
        return decimalFormat.format(number).replace(",", ".")
    }
}