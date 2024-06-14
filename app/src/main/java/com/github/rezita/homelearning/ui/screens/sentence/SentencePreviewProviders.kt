package com.github.rezita.homelearning.ui.screens.sentence

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

class BooleanPreviewProvider : PreviewParameterProvider<Boolean> {
    override val values: Sequence<Boolean>
        get() = sequenceOf(true, false)
}