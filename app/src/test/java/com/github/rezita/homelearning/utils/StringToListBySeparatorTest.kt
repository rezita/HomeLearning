package com.github.rezita.homelearning.utils

import org.junit.Assert
import org.junit.Test

class StringToListBySeparatorTest {

    @Test
    fun stringToList_with_empty_string_empty_separator() {
        val original = ""
        val separator = ""
        val expectedResult = emptyList<String>()
        Assert.assertEquals(original.toListBySeparator(separator), expectedResult)
    }

    @Test
    fun stringToList_with_empty_string_non_empty_separator() {
        val original = ""
        val separator = ","
        val expectedResult = emptyList<String>()
        Assert.assertEquals(original.toListBySeparator(separator), expectedResult)
    }

    @Test
    fun stringToList_with_empty_separator() {
        val original = "firstsecondthirdfourthfifth"
        val separator = ""

        val expectedResult = listOf("firstsecondthirdfourthfifth")
        Assert.assertEquals(original.toListBySeparator(separator), expectedResult)
    }

    @Test
    fun stringToList_doenst_contain_the_separator() {
        val original = "firstsecondthirdfourthfifth"
        val separator = ","
        val expectedResult = listOf("firstsecondthirdfourthfifth")
        Assert.assertEquals(original.toListBySeparator(separator), expectedResult)
    }


    @Test
    fun stringToList_with_one_char_separator() {
        val original = "first,second,third,fourth,fifth"
        val separator = ","
        val expectedResult = listOf("first", "second", "third", "fourth", "fifth")
        Assert.assertEquals(original.toListBySeparator(separator), expectedResult)
    }

    @Test
    fun stringToList_with_longer_separator() {
        val original = "first, second, third, fourth, fifth"
        val separator = ", "
        val expectedResult = listOf("first", "second", "third", "fourth", "fifth")
        Assert.assertEquals(original.toListBySeparator(separator), expectedResult)
    }

    @Test
    fun stringToList_with_no_match_separator() {
        val original = "first, second, third, fourth, fifth"
        val separator = " , "
        val expectedResult = listOf("first, second, third, fourth, fifth")
        Assert.assertEquals(original.toListBySeparator(separator), expectedResult)
    }

    @Test
    fun stringToList_empty_separator_one_item() {
        val original = "first, second"
        val separator = ""
        val expectedResult = listOf("first, second")
        Assert.assertEquals(original.toListBySeparator(separator), expectedResult)
    }
}