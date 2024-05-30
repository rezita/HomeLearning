package com.github.rezita.homelearning.ui.screens.reading

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

class ColorDisplayParameterProvider : PreviewParameterProvider<Boolean> {
    override val values: Sequence<Boolean>
        get() = sequenceOf(true, false)
}