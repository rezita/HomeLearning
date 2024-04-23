package com.github.rezita.homelearning.fake

import com.github.rezita.homelearning.data.NormalRepositoryResult
import com.github.rezita.homelearning.data.RepositoryResult
import com.github.rezita.homelearning.model.ApiFillInSentence
import com.github.rezita.homelearning.model.ApiReadingWord
import com.github.rezita.homelearning.model.ApiSpellingWord
import com.github.rezita.homelearning.model.Category
import com.github.rezita.homelearning.model.GetRequestApiItems
import com.github.rezita.homelearning.model.asFillInSentence
import com.github.rezita.homelearning.model.asReadingWord
import com.github.rezita.homelearning.model.asSpellingWord

object FakeReadingDataSource {
    /**READING*/
    /** Source for Api readingWords @GET request*/
    private val readingAPIWord1 = ApiReadingWord(
        word = "help", comment = "HFW", category = "phase3", rule = ""
    )

    private val readingAPIWord2 = ApiReadingWord(
        word = "she", comment = "tricky words", rule = "ul sh; green e", category = "phase2"
    )

    private val readingAPIWord3 = ApiReadingWord(
        word = "you", comment = "tricky words", rule = "blue ou", category = "phase2"
    )

    private val readingWord1 = readingAPIWord1.asReadingWord()
    private val readingWord2 = readingAPIWord2.asReadingWord()
    private val readingWord3 = readingAPIWord3.asReadingWord()

    /** This will be returned when the FakeWordsApiService.getReadingWords will be called */
    val apiReadingWords =
        GetRequestApiItems(
            items = listOf(readingAPIWord1, readingAPIWord2, readingAPIWord3),
            message = ""
        )

    /** This is what the repository should return */
    val readingWords =
        RepositoryResult.Success(listOf(readingWord1, readingWord2, readingWord3))

}

object FakeSpellingDataSource {
    /**SPELLING*/
    private val spellingApi1 =
        ApiSpellingWord(word = "appear", category = "school", comment = "Y3Y4", result = "0")
    private val spellingApi2 =
        ApiSpellingWord(word = "behave", category = "school", comment = "Y3Y4", result = "0")
    private val spellingApi3 =
        ApiSpellingWord(word = "boutique", category = "school", comment = "Y3Y4", result = "0")
    private val spellingApi4 = ApiSpellingWord(
        word = "destructive",
        category = "home",
        comment = "Y3Y4/individual",
        result = "0"
    )
    val apiSpellingWords =
        GetRequestApiItems(
            items = listOf(spellingApi1, spellingApi2, spellingApi3, spellingApi4),
            message = ""
        )
    val spellingWords = NormalRepositoryResult.Downloaded(
        listOf(
            spellingApi1.asSpellingWord(),
            spellingApi2.asSpellingWord(),
            spellingApi3.asSpellingWord(),
            spellingApi4.asSpellingWord()
        )
    )
}

object FakeSentenceDataSource {
    /**SENTENCE*/
    private val sentenceApi1 = ApiFillInSentence(
        sentence = "I don’t know what’s wrong – I just feel $£.",
        suggestion = "blue / blew",
        solution = "blue",
        answer = "",
        tense = "Y2",
        result = "0"
    )
    private val sentenceApi2 = ApiFillInSentence(
        sentence = "The rational and the irrational $£ each other.",
        suggestion = "complement / compliment",
        solution = "complement",
        answer = "",
        tense = "Y5-6",
        result = "0"
    )

    private val sentenceApi3 = ApiFillInSentence(
        sentence = "It may have an $£ on our plans.",
        suggestion = "affect / effect",
        solution = "effect",
        answer = "",
        tense = "Y3-4",
        result = "0"
    )
    val apiSentences =
        GetRequestApiItems(items = listOf(sentenceApi1, sentenceApi2, sentenceApi3), message = "")

    val sentences = NormalRepositoryResult.Downloaded(
        listOf(
            sentenceApi1.asFillInSentence(),
            sentenceApi2.asFillInSentence(),
            sentenceApi3.asFillInSentence()
        )
    )
}

/**CATEGORY*/
object FakeCategoryDataSource {
    val apiCategories = Category(categories = listOf("school", "home"))

    val categories = RepositoryResult.Success(listOf("school", "home"))
}