package com.github.rezita.homelearning.utils

import org.junit.Assert.assertEquals
import org.junit.Test

class ListJoinWithSeparatorTest {
    @Test
    fun listJoinWithSeparator_list_with_empty_separator() {
        val original = listOf("first", "second", "third", "fourth", "fifth")
        val separator = ""
        val expectedResult = "firstsecondthirdfourthfifth"
        assertEquals(original.joinWithSeparator(separator), expectedResult)
    }

    @Test
    fun listJoinWithSeparator_list_with_non_empty_separator() {
        val original = listOf("first", "second", "third", "fourth", "fifth")
        val separator = ","
        val expectedResult = "first,second,third,fourth,fifth"
        assertEquals(original.joinWithSeparator(separator), expectedResult)

    }

    @Test
    fun listJoinWithSeparator_list_with_longer_separator() {
        val original = listOf("first", "second", "third", "fourth", "fifth")
        val separator = ", "
        val expectedResult = "first, second, third, fourth, fifth"
        assertEquals(original.joinWithSeparator(separator), expectedResult)

    }

    @Test
    fun listJoinWithSeparator_one_element_list_empty_separator() {
        val original = listOf("first")
        val separator = ""
        val expectedResult = "first"
        assertEquals(original.joinWithSeparator(separator), expectedResult)
    }

    @Test
    fun listJoinWithSeparator_one_element_list_with_nonempty_separator() {
        val original = listOf("first")
        val separator = ","
        val expectedResult = "first"
        assertEquals(original.joinWithSeparator(separator), expectedResult)
    }

    @Test
    fun listJoinWithSeparator_one_element_list_with_longer_separator() {
        val original = listOf("first")
        val separator = ", "
        val expectedResult = "first"
        assertEquals(original.joinWithSeparator(separator), expectedResult)
    }

    @Test
    fun listJoinWithSeparator_empty_list_empty_separator() {
        val original = emptyList<String>()
        val separator = ""
        val expectedResult = ""
        assertEquals(original.joinWithSeparator(separator), expectedResult)
    }

    @Test
    fun listJoinWithSeparator_empty_list_non_empty_separator() {
        val original = emptyList<String>()
        val separator = ","
        val expectedResult = ""
        assertEquals(original.joinWithSeparator(separator), expectedResult)
    }

    @Test
    fun listJoinWithSeparator_empty_list_longer_separator() {
        val original = emptyList<String>()
        val separator = ", "
        val expectedResult = ""
        assertEquals(original.joinWithSeparator(separator), expectedResult)
    }
}