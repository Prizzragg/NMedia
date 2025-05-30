package ru.netology.nmedia.dto

object Counter {
    fun shortNote(number: Int): String {
        return when (number) {
            in 0..999 -> "$number"
            in 1000..9999 -> {
                val numberToString = (number / 1000.0).toString()
                if ((number % 1000) < 100) {
                    val result = "${number / 1000}K"
                    return result
                }
                val result =
                    "${numberToString[0]}${numberToString[1]}${numberToString[2]}K"
                result.toString()
            }

            in 10000..999999 -> "${number / 1000}K"
            else -> {
                val numberToString = (number / 1000000.0).toString()
                if ((number % 1000000) < 100000) {
                    val result = "${number / 1000000}M"
                    return result
                }
                val result =
                    "${numberToString[0]}${numberToString[1]}${numberToString[2]}M"
                result.toString()
            }
        }
    }
}