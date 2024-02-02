package com.github.rezita.homelearning.utils

fun ArrayList<String>.joinWithSeparator(separator: String): String{
    return joinToString(separator = separator)
}

fun String.indexOfOrZero(string: String): Int{
    val index = indexOf(string)
    return if (index > 0) index else 0
}