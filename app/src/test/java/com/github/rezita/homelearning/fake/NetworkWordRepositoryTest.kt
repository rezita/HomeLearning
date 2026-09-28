package com.github.rezita.homelearning.fake

import com.github.rezita.homelearning.data.NetworkWordRepository
import com.github.rezita.homelearning.data.RepositoryResult
import com.github.rezita.homelearning.model.SpanishWord
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

class NetworkWordRepositoryTest {

    @get:Rule
    val tempFolder = TemporaryFolder()

    private fun createRepository(scope: CoroutineScope): NetworkWordRepository {
        val configRepository = FakeAppConfigRepository.create(
            scope = scope,
            configFile = File(tempFolder.root, "app_config_test.pb")
        )
        return NetworkWordRepository(
            wordsAPIService = FakeWordsApiService(),
            appConfigDataRepository = configRepository
        )
    }

    @Test
    fun networkWordRepository_getReadingWords_verifyReadingWordsList() =
        runTest {
            val repository = createRepository(backgroundScope)
            assertEquals(
                FakeReadingDataSource.readingWords,
                repository.getReadingWords()
            )
        }

    @Test
    fun networkWordRepository_getCEWWords_verifyCEWWordsList() =
        runTest {
            val repository = createRepository(backgroundScope)
            assertEquals(
                FakeReadingDataSource.readingWords,
                repository.getCEWWords()
            )
        }

    @Test
    fun networkWordRepository_getErikSpelling_verifyWordsList() =
        runTest {
            val repository = createRepository(backgroundScope)
            assertEquals(
                FakeSpellingDataSource.spellingWords,
                repository.getErikSpellingWords()
            )
        }

    @Test
    fun networkWordRepository_getMarkSpelling_verifyWordsList() =
        runTest {
            val repository = createRepository(backgroundScope)
            assertEquals(
                FakeSpellingDataSource.spellingWords,
                repository.getMarkSpellingWords()
            )
        }

    @Test
    fun networkWordRepository_getIrregularVerbs_verifyList() =
        runTest {
            val repository = createRepository(backgroundScope)
            assertEquals(
                FakeSentenceDataSource.sentences,
                repository.getIrregularVerbs()
            )
        }

    @Test
    fun networkWordRepository_getHomophones_verifyList() =
        runTest {
            val repository = createRepository(backgroundScope)
            assertEquals(
                FakeSentenceDataSource.sentences,
                repository.getHomophones()
            )
        }

    @Test
    fun networkWordRepository_getErikCategories_verifyList() =
        runTest {
            val repository = createRepository(backgroundScope)
            assertEquals(
                FakeCategoryDataSource.categories,
                repository.getErikCategories()
            )
        }

    @Test
    fun networkWordRepository_getMarkCategories_verifyList() =
        runTest {
            val repository = createRepository(backgroundScope)
            assertEquals(
                FakeCategoryDataSource.categories,
                repository.getMarkCategories()
            )
        }

    @Test
    fun networkWordRepository_getZitaSpanishWords_verifyList() =
        runTest {
            val repository = createRepository(backgroundScope)
            assertEquals(
                FakeSpanishDataSource.spanishWords(enToSp = true),
                repository.getZitaSpanishWords(enToSp = true)
            )
        }

    @Test
    fun networkWordRepository_getWeekSpanishWords_verifyList() =
        runTest {
            val repository = createRepository(backgroundScope)
            assertEquals(
                FakeSpanishDataSource.spanishWords(enToSp = true),
                repository.getWeekSpanishWords()
            )
        }

    @Test
    fun networkWordRepository_updateZitaSpanishWords_verifySuccess() =
        runTest {
            val repository = createRepository(backgroundScope)
            val answeredWord = SpanishWord(
                wordEn = "dog",
                wordSp = "perro",
                comment = "animals",
                isWeekWord = true,
                answer = "perro"
            )
            assertEquals(
                RepositoryResult.Success("Success"),
                repository.updateZitaSpanishWords(listOf(answeredWord))
            )
        }

    @Test
    fun networkWordRepository_updateZitaSpanishWords_emptyList_verifyError() =
        runTest {
            val repository = createRepository(backgroundScope)
            assertEquals(
                RepositoryResult.Error("No data has given"),
                repository.updateZitaSpanishWords(emptyList())
            )
        }

    @Test
    fun networkWordRepository_saveSpanishWords_verifySuccess() =
        runTest {
            val repository = createRepository(backgroundScope)
            val newWord = SpanishWord(
                wordEn = "house",
                wordSp = "casa",
                comment = "home",
                isWeekWord = true
            )
            assertEquals(
                RepositoryResult.Success("Success"),
                repository.saveSpanishWords(listOf(newWord))
            )
        }
}
