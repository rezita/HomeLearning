package com.github.rezita.homelearning.model

import com.github.rezita.homelearning.network.SheetAction

enum class WordStatus(val value: Int) {
    UNCHECKED(0), CORRECT(1), INCORRECT(-1);

    companion object {
        fun forValue(value: String) = SheetAction.values().find { it.value == value }
    }
}