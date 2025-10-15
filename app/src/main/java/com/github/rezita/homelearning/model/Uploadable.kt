package com.github.rezita.homelearning.model

interface Uploadable {
    fun getDisplayedFields(): List<String>
    fun getBaseProperty(): String
}