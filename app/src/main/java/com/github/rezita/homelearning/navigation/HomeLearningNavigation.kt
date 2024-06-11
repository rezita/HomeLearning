package com.github.rezita.homelearning.navigation

import android.os.Build
import android.os.Bundle
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.network.SheetAction
import com.github.rezita.homelearning.ui.HomeLearningAppState
import com.github.rezita.homelearning.ui.screens.home.HomeLearningTabItem
import com.github.rezita.homelearning.ui.screens.home.HomeScreen
import com.github.rezita.homelearning.ui.screens.home.TabButton
import com.github.rezita.homelearning.ui.screens.home.TabWithButtons
import com.github.rezita.homelearning.ui.screens.reading.ReadingScreen
import com.github.rezita.homelearning.ui.screens.sentence.FillInSentenceSentenceScreen
import com.github.rezita.homelearning.ui.screens.spelling.SpellingScreen
import com.github.rezita.homelearning.ui.screens.uploadwords.UploadWordsScreen

val start_destination = "${Home.route}/0"

/**Navigates back to the start destination with emptying the backstack
 * No navigationBack / navigationUp */
fun NavHostController.navigateStartDestinationWithoutBack() {
    this.navigate(start_destination) {
        popUpTo(
            this@navigateStartDestinationWithoutBack.graph.id
        ) {
            saveState = true
            inclusive = true
        }
    }
}

@Composable
fun HomeLearningNavigation(
    homeLearningAppState: HomeLearningAppState,
    startDestination: String = start_destination,
    modifier: Modifier = Modifier
) {
    val navController = homeLearningAppState.navController

    val erikTabButtons = listOf(
        TabButton(
            titleId = R.string.start_erik_spelling,
            onClick = { navController.navigate(route = "${Spelling.route}/${SheetAction.READ_ERIK_SPELLING_WORDS}") }
        ),
        TabButton(
            titleId = R.string.start_irregularVerbs,
            onClick = { navController.navigate(route = IrregularVerbs.route) }
        ),
        TabButton(
            titleId = R.string.start_homophones,
            onClick = { navController.navigate(route = Homophones.route) }
        ),
        TabButton(
            titleId = R.string.upload_erik_words,
            onClick = { navController.navigate("${Upload.route}/${SheetAction.SAVE_ERIK_WORDS}") }
        ))

    val markTabButtons = listOf(
        TabButton(
            titleId = R.string.start_reading,
            onClick = { navController.navigate("${Reading.route}/${SheetAction.READ_READING_WORDS}") }
        ),
        TabButton(
            titleId = R.string.reading_cew,
            onClick = { navController.navigate("${Reading.route}/${SheetAction.READ_READING_CEW}") }
        ),
        TabButton(
            titleId = R.string.start_mark_spelling,
            onClick = { navController.navigate("${Spelling.route}/${SheetAction.READ_MARK_SPELLING_WORDS}") }
        ),
        TabButton(
            titleId = R.string.upload_mark_words,
            onClick = { navController.navigate("${Upload.route}/${SheetAction.SAVE_MARK_WORDS}") }
        ))

    val erikTabValues = HomeLearningTabItem(
        name = "Erik",
        content = { TabWithButtons(erikTabButtons) },
        onSelected = {
            navController.navigateStartDestinationWithoutBack()
        }
    )
    val markTabValues =
        HomeLearningTabItem(
            name = "Mark",
            content = {
                TabWithButtons(markTabButtons)
            },
            onSelected = { navController.navigate(route = "${Home.route}/1") }
        )

    val tabs = listOf(erikTabValues, markTabValues)


    NavHost(navController = navController, startDestination = startDestination) {

        composable(
            route = Home.routeWithArgs,
            arguments = Home.arguments
        ) { navBackStackEntry ->
            val tabArg = navBackStackEntry.arguments?.getInt("selectedTab") ?: 0
            HomeScreen(
                tabs = tabs,
                selectedTab = tabArg,
                modifier = modifier
                    .fillMaxSize()
                    .padding(dimensionResource(id = R.dimen.padding_big))
            )
        }

        composable(
            route = Spelling.routeWithArgs,
            arguments = Spelling.arguments
        ) { navBackStackEntry ->
            val sheetAction = getSheetActionFromArgs(
                navBackStackEntry.arguments,
                SheetAction.READ_ERIK_SPELLING_WORDS
            )
            SpellingScreen(
                sheetAction = sheetAction,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() },
                addNewCallback = {
                    navController.navigate(
                        "${Upload.route}/${
                            getUploadSheetAction(
                                sheetAction
                            )
                        }"
                    )
                },
                windowSize = homeLearningAppState.windowSizeClass,
                modifier = modifier
            )
        }

        composable(
            route = Reading.routeWithArgs,
            arguments = Reading.arguments
        ) { navBackStackEntry ->
            val sheetAction = getSheetActionFromArgs(
                navBackStackEntry.arguments,
                SheetAction.READ_READING_WORDS
            )
            ReadingScreen(
                sheetAction = sheetAction,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() },
                windowSize = homeLearningAppState.windowSizeClass,
                modifier = modifier
            )
        }

        composable(
            route = Upload.routeWithArgs,
            arguments = Upload.arguments
        ) { navBackStackEntry ->
            val sheetAction = getSheetActionFromArgs(
                navBackStackEntry.arguments,
                SheetAction.SAVE_ERIK_WORDS
            )
            UploadWordsScreen(
                sheetAction = sheetAction,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() },
                modifier = modifier
            )
        }

        composable(route = Homophones.route) {
            FillInSentenceSentenceScreen(
                sheetAction = SheetAction.READ_HOMOPHONES,
                titleId = R.string.homophones_title,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() },
                modifier = modifier
            )
        }

        composable(route = IrregularVerbs.route) {
            FillInSentenceSentenceScreen(
                sheetAction = SheetAction.READ_IRREGULAR_VERBS,
                titleId = R.string.verbs_title,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() },
                modifier = modifier
            )
        }
    }
}

@Suppress("DEPRECATION")
private fun getSheetActionFromArgs(arguments: Bundle?, default: SheetAction): SheetAction {
    val sheetAction =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {    //API33 and above
            arguments?.getSerializable(
                "sheet_action",
                SheetAction::class.java
            ) ?: default
        } else {
            (arguments?.getSerializable("sheet_action")
                ?: SheetAction.SAVE_ERIK_WORDS) as SheetAction
        }
    return sheetAction
}

private fun getUploadSheetAction(currentSheetAction: SheetAction): SheetAction {
    return when (currentSheetAction) {
        SheetAction.READ_ERIK_SPELLING_WORDS -> SheetAction.SAVE_ERIK_WORDS
        SheetAction.READ_MARK_SPELLING_WORDS -> SheetAction.SAVE_MARK_WORDS
        else -> SheetAction.SAVE_ERIK_WORDS
    }
}