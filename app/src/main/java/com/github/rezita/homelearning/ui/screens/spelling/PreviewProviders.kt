package com.github.rezita.homelearning.ui.screens.spelling

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.github.rezita.homelearning.ui.size.height
import com.github.rezita.homelearning.ui.size.wL_min_type1
import com.github.rezita.homelearning.ui.size.wM_min_type1
import com.github.rezita.homelearning.ui.size.wS_min_type1

class SpellingScreenSizeParameterProvider : PreviewParameterProvider<Pair<Int, Int>> {
    override val values: Sequence<Pair<Int, Int>>
        get() = sequenceOf(
            //extra exrtra small
            //Pair(wS_P_min - 100, hS_P_min),
            //xsmall max
            //Pair(wS_P_min - 1, hS_P_min),
            //small min ++
            Pair(wS_min_type1, height),
            //small max
            //Pair(wM_P_min - 1, hS_P_min),
            //medium min ++
            Pair(wM_min_type1, height),
            //medium max
            //Pair(wL_P_min - 1, hS_P_min),
            //large min ++
            Pair(wL_min_type1, height),
            //large max
            //Pair(wxL_P_min - 1, hS_P_min),
            //xlarge min
            //Pair(wxL_P_min, hS_P_min),
        )
}

class SpellingEnableParameterProvider : PreviewParameterProvider<Boolean> {
    override val values: Sequence<Boolean>
        get() = sequenceOf(true, false)
}

class RadioButtonTypeProvider : PreviewParameterProvider<RadioButtonContentType> {
    override val values: Sequence<RadioButtonContentType>
        get() = sequenceOf(
            RadioButtonContentType.BUTTONS_ONLY,
            RadioButtonContentType.BUTTONS_AND_SHORT,
            RadioButtonContentType.BUTTONS_AND_LONG
        )
}