package com.github.rezita.homelearning.ui.screens.upload.common.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.github.rezita.homelearning.R

@Composable
fun NoWordsContent(modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium))) {
        Text(text = stringResource(id = R.string.upload_no_words_message))
    }
}
