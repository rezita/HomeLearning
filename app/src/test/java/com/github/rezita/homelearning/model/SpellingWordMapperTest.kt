package com.github.rezita.homelearning.model

import org.junit.Assert
import org.junit.Test

class SpellingWordMapperTest {

    @Test
    fun test_spellingWord_to_APISpellingWord_correct() {
        val original = SpellingWord(
            word = "this",
            category = "Y1",
            comment = "No Comment",
            status = WordStatus.CORRECT
        )

        val expectedResult = ApiSpellingWord(
            word = "this",
            category = "Y1",
            comment = "No Comment",
            result = "1"
        )
        Assert.assertEquals(expectedResult, original.asAPISellingWord())
    }

    @Test
    fun test_spellingWord_to_APISpellingWord_incorrect() {
        val original = SpellingWord(
            word = "this",
            category = "Y1",
            comment = "No Comment",
            status = WordStatus.INCORRECT
        )

        val expectedResult = ApiSpellingWord(
            word = "this",
            category = "Y1",
            comment = "No Comment",
            result = "-1"
        )
        Assert.assertEquals(expectedResult, original.asAPISellingWord())
    }

    @Test
    fun test_spellingWord_to_APISpellingWord_unchecked() {
        val original = SpellingWord(
            word = "this",
            category = "Y1",
            comment = "No Comment",
            status = WordStatus.UNCHECKED
        )

        val expectedResult = ApiSpellingWord(
            word = "this",
            category = "Y1",
            comment = "No Comment",
            result = "0"
        )
        Assert.assertEquals(expectedResult, original.asAPISellingWord())
    }

    @Test
    fun test_APIspellingWord_to_SpellingWord_no_status() {
        val expectedResult = SpellingWord(
            word = "this",
            category = "Y1",
            comment = "No Comment",
            status = WordStatus.UNCHECKED
        )

        val original = ApiSpellingWord(
            word = "this",
            category = "Y1",
            comment = "No Comment"
        )
        Assert.assertEquals(expectedResult, original.asSpellingWord())
    }

    @Test
    fun test_APIspellingWord_to_SpellingWord_result_1() {
        val expectedResult = SpellingWord(
            word = "this",
            category = "Y1",
            comment = "No Comment",
            status = WordStatus.UNCHECKED
        )

        val original = ApiSpellingWord(
            word = "this",
            category = "Y1",
            comment = "No Comment",
            result = "1"
        )
        Assert.assertEquals(expectedResult, original.asSpellingWord())
    }

    @Test
    fun test_APIspellingWord_to_SpellingWord_result_0() {
        val expectedResult = SpellingWord(
            word = "this",
            category = "Y1",
            comment = "No Comment",
            status = WordStatus.UNCHECKED
        )

        val original = ApiSpellingWord(
            word = "this",
            category = "Y1",
            comment = "No Comment",
            result = "0"
        )
        Assert.assertEquals(expectedResult, original.asSpellingWord())
    }

    @Test
    fun test_APIspellingWord_to_SpellingWord_result_negative() {
        val expectedResult = SpellingWord(
            word = "this",
            category = "Y1",
            comment = "No Comment",
            status = WordStatus.UNCHECKED
        )

        val original = ApiSpellingWord(
            word = "this",
            category = "Y1",
            comment = "No Comment",
            result = "-1"
        )
        Assert.assertEquals(expectedResult, original.asSpellingWord())
    }

    @Test
    fun test_APIspellingWord_to_SpellingWord_result_irrelevan() {
        val expectedResult = SpellingWord(
            word = "this",
            category = "Y1",
            comment = "No Comment",
            status = WordStatus.UNCHECKED
        )

        val original = ApiSpellingWord(
            word = "this",
            category = "Y1",
            comment = "No Comment",
            result = "23"
        )
        Assert.assertEquals(expectedResult, original.asSpellingWord())
    }
}