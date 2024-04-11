package com.github.rezita.homelearning.fake

import com.github.rezita.homelearning.data.ComplexRepositoryResult
import com.github.rezita.homelearning.data.NormalRepositoryResult
import com.github.rezita.homelearning.data.SimpleRepositoryResult
import com.github.rezita.homelearning.data.WordRepository
import com.github.rezita.homelearning.model.FillInSentence
import com.github.rezita.homelearning.model.ReadingWord
import com.github.rezita.homelearning.model.SpellingWord

class FakeNetworkWorkRepository : WordRepository {
    override suspend fun getReadingWords(): SimpleRepositoryResult<ReadingWord> {
        return FakeReadingDataSource.readingWords
    }

    override suspend fun getCEWWords(): SimpleRepositoryResult<ReadingWord> {
        return FakeReadingDataSource.readingWords
    }

    override suspend fun getIrregularVerbs(): NormalRepositoryResult<FillInSentence> {
        return FakeSentenceDataSource.sentences
    }

    override suspend fun getHomophones(): NormalRepositoryResult<FillInSentence> {
        return FakeSentenceDataSource.sentences
    }

    override suspend fun getErikSpellingWords(): NormalRepositoryResult<SpellingWord> {
        return FakeSpellingDataSource.spellingWords
    }

    override suspend fun getMarkSpellingWords(): NormalRepositoryResult<SpellingWord> {
        return FakeSpellingDataSource.spellingWords
    }

    override suspend fun getErikCategories(): ComplexRepositoryResult<String, SpellingWord> {
        return FakeCategoryDataSource.categories
    }

    override suspend fun getMarkCategories(): ComplexRepositoryResult<String, SpellingWord> {
        return FakeCategoryDataSource.categories
    }

    override suspend fun updateIrregularVerbs(sentences: List<FillInSentence>): NormalRepositoryResult<FillInSentence> {
        TODO("Not yet implemented")
    }

    override suspend fun updateHomophones(sentences: List<FillInSentence>): NormalRepositoryResult<FillInSentence> {
        TODO("Not yet implemented")
    }

    override suspend fun updateErikSpellingWords(words: List<SpellingWord>): NormalRepositoryResult<SpellingWord> {
        TODO("Not yet implemented")
    }

    override suspend fun updateMarkSpellingWords(words: List<SpellingWord>): NormalRepositoryResult<SpellingWord> {
        TODO("Not yet implemented")
    }

    override suspend fun saveErikSpellingWords(
        uploadable: List<SpellingWord>,
        downloaded: List<String>
    ): ComplexRepositoryResult<String, SpellingWord> {
        TODO("Not yet implemented")
    }

    override suspend fun saveMarkSpellingWords(
        uploadable: List<SpellingWord>,
        downloaded: List<String>
    ): ComplexRepositoryResult<String, SpellingWord> {
        TODO("Not yet implemented")
    }
}