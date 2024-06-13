package com.github.rezita.homelearning.ui.screens.common

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

class BooleanParameterProvider : PreviewParameterProvider<Boolean> {
    override val values: Sequence<Boolean>
        get() = sequenceOf(true, false)
}