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
data class Upload(val sheetAction: SheetAction) : HomeLearningDestination
