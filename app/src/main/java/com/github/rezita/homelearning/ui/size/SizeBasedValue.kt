package com.github.rezita.homelearning.ui.size

interface SizedValue<T> {
    operator fun invoke(size: HomeLearningWidthClass): T
}

class WidthSizeBasedValue<T>(
    private val xsmall: T,
    private val small: T,
    private val medium: T,
    private val large: T,
    private val xlarge: T
) : SizedValue<T> {
    override operator fun invoke(sizeClass: HomeLearningWidthClass): T {
        return when (sizeClass) {
            HomeLearningWidthClass.XSMALL -> xsmall
            HomeLearningWidthClass.SMALL -> small
            HomeLearningWidthClass.MEDIUM -> medium
            HomeLearningWidthClass.LARGE -> large
            HomeLearningWidthClass.XLARGE -> xlarge
        }
    }
}