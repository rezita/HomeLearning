package com.github.rezita.homelearning.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.github.rezita.homelearning.network.SheetAction

interface HomeLearningDestination {
    val route: String
}


object Home : HomeLearningDestination {
    override val route = "Home"
}

object Spelling : HomeLearningDestination {
    override val route = "spelling"
    private const val sheetActionArg = "sheet_action"
    val routeWithArgs = "${route}/{${sheetActionArg}}"
    val arguments = listOf(
        navArgument(sheetActionArg) { type = NavType.EnumType(SheetAction::class.java) }
    )
}

object Homophones : HomeLearningDestination {
    override val route = "homophones"
}

object IrregularVerbs : HomeLearningDestination {
    override val route = "irregular_verbs"
}

object Reading : HomeLearningDestination {
    override val route = "reading"
    private const val sheetActionArg = "sheet_action"
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