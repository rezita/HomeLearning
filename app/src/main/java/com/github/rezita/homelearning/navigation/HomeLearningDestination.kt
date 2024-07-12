package com.github.rezita.homelearning.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.github.rezita.homelearning.network.SheetAction

interface HomeLearningDestination {
    val route: String
}


object Home : HomeLearningDestination {
    override val route = "Home"
    private const val tabArg = "selectedTab"
    val routeWithArgs = "${route}/{${tabArg}}"
    val arguments = listOf(
        navArgument(tabArg) { type = NavType.IntType }
    )
}

object SpellingDestination : HomeLearningDestination {
    override val route = "spelling"
    const val sheetActionArg = "sheet_action"
    val routeWithArgs = "${route}/{${sheetActionArg}}"
    val arguments = listOf(
        navArgument(sheetActionArg) { type = NavType.EnumType(SheetAction::class.java) }
    )
}

object SentenceDestination : HomeLearningDestination {
    override val route = "sentence"
    const val sheetActionArg = "sheet_action"
    val routeWithArgs = "${route}/{${sheetActionArg}}"
    val arguments = listOf(
        navArgument(sheetActionArg) { type = NavType.EnumType(SheetAction::class.java) }
    )
}

object ReadingDestination : HomeLearningDestination {
    override val route = "reading"
    const val sheetActionArg = "sheet_action"
    val routeWithArgs = "${route}/{${sheetActionArg}}"
    val arguments = listOf(
        navArgument(sheetActionArg) { type = NavType.EnumType(SheetAction::class.java) }
    )
}

object Upload : HomeLearningDestination {
    override val route = "upload_words"
    private const val sheetActionArg = "sheet_action"
    val routeWithArgs = "${route}/{${sheetActionArg}}"
    val arguments = listOf(
        navArgument(sheetActionArg) { type = NavType.EnumType(SheetAction::class.java) }
    )
}