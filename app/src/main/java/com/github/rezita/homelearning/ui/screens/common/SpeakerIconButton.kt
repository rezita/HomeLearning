package com.github.rezita.homelearning.ui.screens.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.RecordVoiceOver
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.github.rezita.homelearning.R

@Composable
fun SpeakerIconButton(
    modifier: Modifier = Modifier,
    onClickCallback: () -> Unit
) {
    IconButton(
        onClick = { onClickCallback() }
    ) {
        Icon(
            imageVector = Icons.Rounded.RecordVoiceOver,
            contentDescription = stringResource(id = R.string.spelling_text_to_speech),
            modifier = modifier
        )
    }
}
