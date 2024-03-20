package com.github.rezita.homelearning.utils

import org.junit.Assert.assertEquals
import org.junit.Test

class StringIndexOrZeroTest {

    @Test
    fun stringIndexOrZero_contains_beginning_one_char() {
        val originalString = "contains"
        val string = "c"
        val expectedResult = 0
        assertEquals(originalString.indexOfOrZero(string), expectedResult)
    }

    @Test
    fun stringIndexOrZero_contains_beginning_more_chars() {
        val originalString = "contains"
        val string = "con"
        val expectedResult = 0
        assertEquals(originalString.indexOfOrZero(string), expectedResult)
    }

    @Test
    fun stringIndexOrZero_contains_middle_one_char() {
        val originalString = "contains"
        val string = "t"
        val expectedResult = 3
        assertEquals(originalString.indexOfOrZero(string), expectedResult)
    }

    @Test
    fun stringIndexOrZero_contains_middle_more_chars() {
        val originalString = "contains"
        val string = "tai"
        val expectedResult = 3
        assertEquals(originalString.indexOfOrZero(string), expectedResult)
    }

    @Test
    fun stringIndexOrZero_contains_end_one_char() {
        val originalString = "contains"
        val string = "s"
        val expectedResult = 7
        assertEquals(originalString.indexOfOrZero(string), expectedResult)
    }

    @Test
    fun stringIndexOrZero_contains_end_more_chars() {
        val originalString = "contains"
        val string = "ins"
        val expectedResult = 5
        assertEquals(originalString.indexOfOrZero(string), expectedResult)
    }

    @Test
    fun stringIndexOrZero_contains_first_appearance() {
        val originalString = "contains"
        val string = "n"
        val expectedResult = 2
        assertEquals(originalString.indexOfOrZero(string), expectedResult)
    }

    @Test
    fun stringIndexOrZero_not_contains() {
        val originalString = "contains"
        val string = "no"
        val expectedResult = 0
        assertEquals(originalString.indexOfOrZero(string), expectedResult)
    }
}