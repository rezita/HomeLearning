package com.github.rezita.homelearning.ui.screens.spelling

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
import com.github.rezita.homelearning.model.SpellingWord
import com.github.rezita.homelearning.ui.screens.common.LearningAppBar
import com.github.rezita.homelearning.ui.theme.HomeLearningTheme

@Composable
fun SpellingTopAppBar(
    state: NormalRepositoryResult<SpellingWord>,
    saveCallback: () -> Unit,
    addNewCallback: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    LearningAppBar(
        titleText = when (state) {
            is NormalRepositoryResult.Downloaded -> stringResource(id = R.string.activity_spelling)
            is NormalRepositoryResult.Downloading -> stringResource(id = R.string.app_bar_loading_title)
            is NormalRepositoryResult.DownloadingError -> stringResource(id = R.string.app_bar_error_title)
            is NormalRepositoryResult.Uploading -> stringResource(id = R.string.app_bar_uploading_title)
            is NormalRepositoryResult.Uploaded -> stringResource(id = R.string.activity_spelling)
            is NormalRepositoryResult.UploadError -> stringResource(id = R.string.app_bar_error_title)
        },

        navigateUp = {},

        actions = {
            if (state is NormalRepositoryResult.Downloaded || state is NormalRepositoryResult.UploadError) {
                IconButton(onClick = saveCallback) {
                    Icon(
                        painterResource(id = R.drawable.ic_save_result),
                        contentDescription = stringResource(id = R.string.spelling_save),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
            if (state !is NormalRepositoryResult.Downloading && state !is NormalRepositoryResult.Uploading) {
                IconButton(onClick = { addNewCallback() }) {
                    Icon(
                        painterResource(id = R.drawable.ic_menu_add),
                        contentDescription = stringResource(id = R.string.spelling_add_new_word),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        },
        modifier = modifier
    )
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SpellingTopAppBar_loading() {
    HomeLearningTheme {
        SpellingTopAppBar(
            state = NormalRepositoryResult.Downloading(),
            saveCallback = {},
            addNewCallback = {}
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SentenceTopAppBar_downloaded_success() {
    HomeLearningTheme {
        SpellingTopAppBar(
            state = NormalRepositoryResult.Downloaded(emptyList()),
            saveCallback = {},
            addNewCallback = {}
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SentenceTopAppBar_download_error() {
    HomeLearningTheme {
        SpellingTopAppBar(
            state = NormalRepositoryResult.DownloadingError(""),
            saveCallback = {},
            addNewCallback = {}
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SentenceTopAppBar_uploading() {
    HomeLearningTheme {
        SpellingTopAppBar(
            state = NormalRepositoryResult.Uploading(emptyList()),
            saveCallback = {},
            addNewCallback = {}
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SentenceTopAppBar_upload_success() {
    HomeLearningTheme {
        SpellingTopAppBar(
            state = NormalRepositoryResult.Uploaded(emptyList(), ""),
            saveCallback = {},
            addNewCallback = {}
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SentenceTopAppBar_uploadError() {
    HomeLearningTheme {
        SpellingTopAppBar(
            state = NormalRepositoryResult.UploadError(emptyList(), ""),
            saveCallback = {},
            addNewCallback = {}
        )
    }
}