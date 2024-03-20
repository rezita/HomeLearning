package com.github.rezita.homelearning.fake

import com.github.rezita.homelearning.data.WordRepository
import com.github.rezita.homelearning.model.FillInSentence
import com.github.rezita.homelearning.model.ReadingWord
import com.github.rezita.homelearning.model.SpellingWord

class FakeNetworkWorkRepository: WordRepository {
    override suspend fun getReadingWords(): List<ReadingWord> {
        return FakeReadingDataSource.readingWords
    }

    override suspend fun getCEWWords(): List<ReadingWord> {
        return FakeReadingDataSource.readingWords
    }

    override suspend fun getIrregularVerbs(): List<FillInSentence> {
        return FakeSentenceDataSource.sentences
    }

    override suspend fun getHomophones(): List<FillInSentence> {
        return FakeSentenceDataSource.sentences
    }

    override suspend fun getErikSpellingWords(): List<SpellingWord> {
        return FakeSpellingDataSource.spellingWords
    }

    override suspend fun getMarkSpellingWords(): List<SpellingWord> {
        return FakeSpellingDataSource.spellingWords
    }

    override suspend fun getErikCategories(): List<String> {
        return FakeCategoryDataSource.categories
    }

    override suspend fun getMarkCategories(): List<String> {
        return FakeCategoryDataSource.categories
    }

    override suspend fun updateIrregularVerbs(sentences: List<FillInSentence>): String {
        TODO("Not yet implemented")
    }

    override suspend fun updateHomophones(sentences: List<FillInSentence>): String {
        TODO("Not yet implemented")
    }

    override suspend fun updateErikSpellingWords(words: List<SpellingWord>): String {
        TODO("Not yet implemented")
    }

    override suspend fun updateMarkSpellingWords(words: List<SpellingWord>): String {
        TODO("Not yet implemented")
    }

    override suspend fun saveErikSpellingWords(words: List<SpellingWord>): String {
        TODO("Not yet implemented")
    }

    override suspend fun saveMarkSpellingWords(words: List<SpellingWord>): String {
        TODO("Not yet implemented")
    }
}