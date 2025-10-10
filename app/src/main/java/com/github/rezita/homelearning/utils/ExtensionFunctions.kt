package com.github.rezita.homelearning.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.TextUnit

fun List<String>.joinWithSeparator(separator: String): String {
    return joinToString(separator = separator)
}

fun String.indexOfOrZero(string: String): Int {
    val index = indexOf(string)
    return if (index > 0) index else 0
}

fun String.toListBySeparator(separator: String): List<String> {
    if (this.isEmpty()) {
        return emptyList()
    }
    if (separator.isEmpty()) {
        return listOf(this)
    }
    return this.split(separator).map { it.trim() }
}

@Composable
fun TextUnit.toDp() = with(LocalDensity.current) { toDp() }

fun String.simplify() :String {
    return this.replace(Regex("[!?¡¿.]"), "").trim()
}