package com.hyvu.thuytrangmarket.utils

object NumberUtils {
    fun formatNumber(number: Double): String {
        val numberString = number.toString()
        if (numberString.length <= 3) {
            return numberString
        }

        val reversedString = numberString.reversed()
        val formattedReversed = StringBuilder()

        for (i in reversedString.indices) {
            formattedReversed.append(reversedString[i])
            if ((i + 1) % 3 == 0 && i != reversedString.length - 1) {
                formattedReversed.append(".")
            }
        }

        return formattedReversed.reversed().toString()
    }
}