package com.github.rezita.homelearning.navigation

import com.github.rezita.homelearning.network.SheetAction
import kotlinx.serialization.Serializable

interface HomeLearningDestination

@Serializable
data class Home(val tab: Int = 0) : HomeLearningDestination

@Serializable
data class SpellingDestination(val sheetAction: SheetAction) : HomeLearningDestination

@Serializable
data class SentenceDestination(val sheetAction: SheetAction) : HomeLearningDestination

@Serializable
data class ReadingDestination(val sheetAction: SheetAction) : HomeLearningDestination

@Serializable
data class SpellingUploadDestination(val sheetAction: SheetAction) : HomeLearningDestination

@Serializable
data class SpanishDestination(val sheetAction: SheetAction, val enToSp: Boolean?) :
    HomeLearningDestination

@Serializable
data class SpanishUploadDestination(val sheetAction: SheetAction) : HomeLearningDestination
