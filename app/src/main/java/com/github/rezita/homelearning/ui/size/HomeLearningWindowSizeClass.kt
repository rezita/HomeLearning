package com.github.rezita.homelearning.ui.size

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp

data class HomeLearningWindowSizeClass(
    val widthClassType1: HomeLearningWidthClass,
    val widthClassType2: HomeLearningWidthClass
) {
    companion object {
        fun calculateFromSize(size: DpSize): HomeLearningWindowSizeClass {
            val windowWidthClassType1 =
                HomeLearningWidthClass.getWithClassByType(size.width, ScreenType.TYPE_1)
            val windowWidthClassType2 =
                HomeLearningWidthClass.getWithClassByType(size.width, ScreenType.TYPE_2)
            return HomeLearningWindowSizeClass(windowWidthClassType1, windowWidthClassType2)
        }
    }
}

/**
 * Type1: Spelling Screen and Upload Spelling Screen
 * Type2: Reading Screen
 */
enum class ScreenType { TYPE_1, TYPE_2 }

enum class HomeLearningWidthClass(val minWidthDpType1: Dp, val minWidthDpType2: Dp) {
    XSMALL(wxS_min_type1.dp, wxS_min_type2.dp),
    SMALL(wS_min_type1.dp, wS_min_type2.dp),
    MEDIUM(wM_min_type1.dp, wM_min_type2.dp),
    LARGE(wL_min_type1.dp, wL_min_type2.dp),
    XLARGE(wxL_min_type1.dp, wxL_min_type2.dp);

    companion object {
        fun getWithClassByType(widthInDp: Dp, screenType: ScreenType): HomeLearningWidthClass {
            return if (screenType == ScreenType.TYPE_1) {
                when {
                    widthInDp < SMALL.minWidthDpType1 -> XSMALL
                    widthInDp < MEDIUM.minWidthDpType1 -> SMALL
                    widthInDp < LARGE.minWidthDpType1 -> MEDIUM
                    widthInDp < XLARGE.minWidthDpType1 -> LARGE
                    else -> XLARGE
                }
            } else {    //Type_2
                when {
                    widthInDp < SMALL.minWidthDpType2 -> XSMALL
                    widthInDp < MEDIUM.minWidthDpType2 -> SMALL
                    widthInDp < LARGE.minWidthDpType2 -> MEDIUM
                    widthInDp < XLARGE.minWidthDpType2 -> LARGE
                    else -> XLARGE
                }
            }
        }
    }
}