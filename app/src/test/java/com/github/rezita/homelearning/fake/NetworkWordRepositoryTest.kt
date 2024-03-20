package com.github.rezita.homelearning.fake

import com.github.rezita.homelearning.data.NetworkWordRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class NetworkWordRepositoryTest {

    @Test
    fun networkWordRepository_getReadingWords_verifyReadingWordsList() =
        runTest {
            val repository = NetworkWordRepository(wordsAPIService = FakeWordsApiService())
            assertEquals(
                repository.getReadingWords(),
                FakeReadingDataSource.readingWords
            )
        }

    @Test
    fun networkWordRepository_getCEWWords_verifyCEWWordsList() =
        runTest {
            val repository = NetworkWordRepository(wordsAPIService = FakeWordsApiService())
            assertEquals(
                repository.getReadingWords(),
                FakeReadingDataSource.readingWords
            )
        }

    @Test
    fun networkWordRepository_getErikSpelling_verifyWordsList() =
        runTest {
            val repository = NetworkWordRepository(wordsAPIService = FakeWordsApiService())
            assertEquals(
                repository.getErikSpellingWords(),
                FakeSpellingDataSource.spellingWords
            )
        }

    @Test
    fun networkWordRepository_getMarkSpelling_verifyWordsList() =
        runTest {
            val repository = NetworkWordRepository(wordsAPIService = FakeWordsApiService())
            assertEquals(
                repository.getMarkSpellingWords(),
                FakeSpellingDataSource.spellingWords
            )
        }

    @Test
    fun networkWordRepository_getIrregularVerbs_verifyList() =
        runTest {
            val repository = NetworkWordRepository(wordsAPIService = FakeWordsApiService())
            assertEquals(
                repository.getIrregularVerbs(),
                FakeSentenceDataSource.sentences
            )
        }

    @Test
    fun networkWordRepository_getHomophones_verifyList() =
        runTest {
            val repository = NetworkWordRepository(wordsAPIService = FakeWordsApiService())
            assertEquals(
                repository.getIrregularVerbs(),
                FakeSentenceDataSource.sentences
            )
        }

    @Test
    fun networkWordRepository_getErikCategories_verifyList() =
        runTest {
            val repository = NetworkWordRepository(wordsAPIService = FakeWordsApiService())
            assertEquals(
                repository.getErikCategories(),
                FakeCategoryDataSource.categories
            )
        }

    @Test
    fun networkWordRepository_getMarkCategories_verifyList() =
        runTest {
            val repository = NetworkWordRepository(wordsAPIService = FakeWordsApiService())
            assertEquals(
                repository.getMarkCategories(),
                FakeCategoryDataSource.categories
            )
        }

}