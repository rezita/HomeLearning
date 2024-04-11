package com.github.rezita.homelearning.ui.viewmodel

import com.github.rezita.homelearning.data.NormalRepositoryResult
import com.github.rezita.homelearning.fake.FakeNetworkWorkRepository
import com.github.rezita.homelearning.model.FillInSentence
import com.github.rezita.homelearning.network.SheetAction
import com.github.rezita.homelearning.ui.viewmodels.FillInSentenceViewModel
import kotlinx.coroutines.test.runTest
import org.junit.Test

class FillInSentenceViewModelTest {
    @Test
    fun updateAnswer_test_valid_data() {
        runTest{
            val sentence1 = FillInSentence(
                sentence = "I have never been to Italy.",
                suggestion = "be",
                solutions = listOf("been"),
                answer = ""
            )
            val sentence2 =
                FillInSentence(
                    sentence = "I have \$£ dinner yet again!",
                    suggestion = "burn",
                    solutions = listOf("burned", "burnt"),
                    answer = ""
                )

            val data = listOf(sentence1, sentence2)
            val state: NormalRepositoryResult<FillInSentence> = NormalRepositoryResult.Downloaded(data)
            val viewmodel = FillInSentenceViewModel(
                wordRepository = FakeNetworkWorkRepository(),
                sheetAction = SheetAction.READ_HOMOPHONES
            )
        }
    }
}