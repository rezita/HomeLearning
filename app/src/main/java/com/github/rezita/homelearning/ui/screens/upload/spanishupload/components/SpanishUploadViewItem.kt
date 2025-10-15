@file:JvmName("SpanishUploadItemKt")

package com.github.rezita.homelearning.ui.screens.upload.spanishupload.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.model.SpanishWord
import com.github.rezita.homelearning.ui.screens.upload.common.components.DeleteConfirmDialog
import com.github.rezita.homelearning.ui.screens.upload.common.components.UserInteractionIcons
import com.github.rezita.homelearning.ui.theme.HomeLearningTheme

@Composable
fun SpanishUploadViewItem(
    word: SpanishWord,
    onDeleteCallback: () -> Unit,
    onEditCallback: () -> Unit,
    modifier: Modifier = Modifier
) {
    val openConfirmDialog = remember { mutableStateOf(false) }

    when {
        openConfirmDialog.value ->
            DeleteConfirmDialog(
                onDismissRequest = {
                    openConfirmDialog.value = false
                },
                onConfirmation = {
                    onDeleteCallback()
                    openConfirmDialog.value = false
                },
            )
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = dimensionResource(id = R.dimen.padding_small)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        SpanishUploadWordItem(word = word, modifier = Modifier.weight(1f))
        UserInteractionIcons(
            onDeleteCallback = { openConfirmDialog.value = true },
            onEditCallback = { onEditCallback() })
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun SpanishUploadItemDisplayPreview() {
    val word = SpanishWord(
        wordEn = "the apple",
        wordSp = "la manzana",
        comment = "with article",
        isWeekWord = false
    )
    HomeLearningTheme {
        Scaffold {
            SpanishUploadViewItem(
                word = word,
                onDeleteCallback = {},
                onEditCallback = {},
                modifier = Modifier.padding(it)
            )
        }
    }
}
