package com.github.rezita.homelearning.fake

import com.github.rezita.homelearning.data.RepositoryResult
import com.github.rezita.homelearning.data.WordRepository
import com.github.rezita.homelearning.model.FillInSentence
import com.github.rezita.homelearning.model.ReadingWord
import com.github.rezita.homelearning.model.SpellingWord

class FakeNetworkWorkRepository : WordRepository {
    override suspend fun getReadingWords(): RepositoryResult<List<ReadingWord>> {
        return FakeReadingDataSource.readingWords
    }

    override suspend fun getCEWWords(): RepositoryResult<List<ReadingWord>> {
        return FakeReadingDataSource.readingWords
    }

    override suspend fun getIrregularVerbs(): RepositoryResult<List<FillInSentence>> {
        return FakeSentenceDataSource.sentences
    }

    override suspend fun getHomophones(): RepositoryResult<List<FillInSentence>> {
        return FakeSentenceDataSource.sentences
    }

    override suspend fun getErikSpellingWords(): RepositoryResult<List<SpellingWord>> {
        return FakeSpellingDataSource.spellingWords
    }

    override suspend fun getMarkSpellingWords(): RepositoryResult<List<SpellingWord>> {
        return FakeSpellingDataSource.spellingWords
    }

    override suspend fun getErikCategories(): RepositoryResult<List<String>> {
        return FakeCategoryDataSource.categories
    }

    override suspend fun getMarkCategories(): RepositoryResult<List<String>> {
        return FakeCategoryDataSource.categories
    }

    override suspend fun updateIrregularVerbs(sentences: List<FillInSentence>): RepositoryResult<String> {
        TODO("Not yet implemented")
    }

    override suspend fun updateHomophones(sentences: List<FillInSentence>): RepositoryResult<String> {
        TODO("Not yet implemented")
    }

    override suspend fun updateErikSpellingWords(words: List<SpellingWord>): RepositoryResult<String> {
        TODO("Not yet implemented")
    }

    override suspend fun updateMarkSpellingWords(words: List<SpellingWord>): RepositoryResult<String> {
        TODO("Not yet implemented")
    }

    override suspend fun saveErikSpellingWords(
        words: List<SpellingWord>
    ): RepositoryResult<String> {
        TODO("Not yet implemented")
    }

    override suspend fun saveMarkSpellingWords(
        words: List<SpellingWord>
    ): RepositoryResult<String> {
        TODO("Not yet implemented")
    }
}