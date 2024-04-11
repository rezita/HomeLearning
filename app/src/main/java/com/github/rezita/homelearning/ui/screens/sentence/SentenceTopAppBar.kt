package com.github.rezita.homelearning.ui.screens.sentence

import android.content.res.Configuration
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.data.NormalRepositoryResult
import com.github.rezita.homelearning.model.FillInSentence
import com.github.rezita.homelearning.ui.screens.common.LearningAppBar
import com.github.rezita.homelearning.ui.theme.HomeLearningTheme

@Composable
fun SentenceTopAppBar(
    state: NormalRepositoryResult<FillInSentence>,
    callback: (Boolean) -> Unit,
    isAllAnswered: Boolean,
    modifier: Modifier = Modifier
) {
    LearningAppBar(
        titleText = when (state) {
            is NormalRepositoryResult.Downloaded -> stringResource(id = R.string.activity_sentences)
            is NormalRepositoryResult.Downloading -> stringResource(id = R.string.app_bar_loading_title)
            is NormalRepositoryResult.DownloadingError -> stringResource(id = R.string.app_bar_error_title)
            is NormalRepositoryResult.Uploading -> stringResource(id = R.string.app_bar_uploading_title)
            is NormalRepositoryResult.Uploaded -> stringResource(id = R.string.activity_sentences)
            is NormalRepositoryResult.UploadError -> stringResource(id = R.string.app_bar_error_title)
        },

        navigateUp = {},

        actions = {
            if (state is NormalRepositoryResult.Downloaded || state is NormalRepositoryResult.UploadError) {
                if (isAllAnswered) {
                    IconButton(onClick = { callback(false) }) {
                        Icon(
                            painterResource(id = R.drawable.baseline_spellcheck_24),
                            contentDescription = stringResource(id = R.string.reading_black_display),
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
        },
        modifier = modifier
    )
}


@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SentenceTopAppBar_success_notAllAnswered() {
    HomeLearningTheme {
        SentenceTopAppBar(
            state = NormalRepositoryResult.Downloaded(emptyList()),
            callback = {},
            isAllAnswered = false
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SentenceTopAppBar_success_allAnswered() {
    HomeLearningTheme {
        SentenceTopAppBar(
            state = NormalRepositoryResult.Downloaded(emptyList()),
            callback = {},
            isAllAnswered = true
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SentenceTopAppBar_loading() {
    HomeLearningTheme {
        SentenceTopAppBar(
            state = NormalRepositoryResult.Downloading(),
            callback = {},
            isAllAnswered = true
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SentenceTopAppBar_error() {
    HomeLearningTheme {
        SentenceTopAppBar(
            state = NormalRepositoryResult.UploadError(emptyList(), ""),
            callback = {},
            isAllAnswered = true
        )
    }
}