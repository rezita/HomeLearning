package com.github.rezita.homelearning.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.network.SheetAction
import com.github.rezita.homelearning.ui.screens.home.HomeLearningTabItem
import com.github.rezita.homelearning.ui.screens.home.HomeScreen
import com.github.rezita.homelearning.ui.screens.home.TabButton
import com.github.rezita.homelearning.ui.screens.home.TabWithButtons
import com.github.rezita.homelearning.ui.screens.reading.ReadingRoute
import com.github.rezita.homelearning.ui.screens.sentence.FillInSentenceSentenceRoute
import com.github.rezita.homelearning.ui.screens.spanish.SpanishRoute
import com.github.rezita.homelearning.ui.screens.spelling.SpellingRoute
import com.github.rezita.homelearning.ui.screens.upload.spanishupload.SpanishUploadRoute
import com.github.rezita.homelearning.ui.screens.upload.spellingupload.SpellingUploadRoute
import com.github.rezita.homelearning.ui.size.HomeLearningWindowSizeClass

val start_destination = Home(0)

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
    windowSizeClass: HomeLearningWindowSizeClass,
    modifier: Modifier = Modifier,
    startDestination: HomeLearningDestination = start_destination
) {
    val navController = rememberNavController()

    val erikTabButtons = listOf(
        TabButton(
            titleId = R.string.start_spanish_week_words,
            onClick = {
                navController.navigate(
                    SpanishDestination(
                        SheetAction.READ_WEEK_SPANISH_WORDS,
                        true
                    )
                )
            }
        ),
        TabButton(
            titleId = R.string.start_erik_spelling,
            onClick = { navController.navigate(route = SpellingDestination(SheetAction.READ_ERIK_SPELLING_WORDS)) }
        ),
        TabButton(
            titleId = R.string.start_irregularVerbs,
            onClick = { navController.navigate(route = SentenceDestination(SheetAction.READ_IRREGULAR_VERBS)) }
        ),
        TabButton(
            titleId = R.string.start_homophones,
            onClick = { navController.navigate(route = SentenceDestination(SheetAction.READ_HOMOPHONES)) }
        ),
        TabButton(
            titleId = R.string.upload_erik_words,
            onClick = { navController.navigate(SpellingUploadDestination(SheetAction.SAVE_ERIK_WORDS)) }
        )
    )


    val markTabButtons = listOf(
        TabButton(
            titleId = R.string.start_reading,
            onClick = { navController.navigate(ReadingDestination(SheetAction.READ_READING_WORDS)) }
        ),
        TabButton(
            titleId = R.string.reading_cew,
            onClick = { navController.navigate(ReadingDestination(SheetAction.READ_READING_CEW)) }
        ),
        TabButton(
            titleId = R.string.start_mark_spelling,
            onClick = { navController.navigate(SpellingDestination(SheetAction.READ_MARK_SPELLING_WORDS)) }
        ),
        TabButton(
            titleId = R.string.upload_mark_words,
            onClick = { navController.navigate(SpellingUploadDestination(SheetAction.SAVE_MARK_WORDS)) }
        )
    )

    val zitaTabButtons = listOf(
        TabButton(
            titleId = R.string.start_en_to_sp,
            onClick = {
                navController.navigate(
                    SpanishDestination(
                        SheetAction.READ_ZITA_SPANISH_WORDS,
                        true
                    )
                )
            }
        ),
        TabButton(
            titleId = R.string.start_sp_to_en,
            onClick = {
                navController.navigate(
                    SpanishDestination(
                        SheetAction.READ_ZITA_SPANISH_WORDS,
                        false
                    )
                )
            }
        ),
        TabButton(
            titleId = R.string.start_rand_spanish,
            onClick = {
                navController.navigate(
                    SpanishDestination(
                        SheetAction.READ_ZITA_SPANISH_WORDS,
                        null
                    )
                )
            }
        ),
        TabButton(
            titleId = R.string.start_read_spanish_en_to_sp,
            onClick = {
                navController.navigate(
                    SpanishDestination(
                        SheetAction.READ_SPANISH_WORDS,
                        true
                    )
                )
            }
        ),
        TabButton(
            titleId = R.string.start_read_spanish_sp_to_en,
            onClick = {
                navController.navigate(
                    SpanishDestination(
                        SheetAction.READ_SPANISH_WORDS,
                        false
                    )
                )
            }
        ),

        TabButton(
            titleId = R.string.upload_spanish,
            onClick = {
                navController.navigate(
                    SpanishUploadDestination(
                        SheetAction.SAVE_ZITA_SPANISH_WORDS
                    )
                )
            }
        ),
    )

    val erikTabValues = HomeLearningTabItem(
        name = stringResource(R.string.tab_Erik),
        content = { TabWithButtons(erikTabButtons) },
        onSelected = {
            navController.navigateStartDestinationWithoutBack()
        }
    )
    val markTabValues =
        HomeLearningTabItem(
            name = stringResource(R.string.tab_Mark),
            content = {
                TabWithButtons(markTabButtons)
            },
            onSelected = { navController.navigate(route = Home(1)) }
        )

    val zitaTabValues = HomeLearningTabItem(
        name = stringResource(R.string.tab_Zita),
        content = { TabWithButtons(zitaTabButtons) },
        onSelected = { navController.navigate(route = Home(2)) }
    )

    val tabs = listOf(erikTabValues, markTabValues, zitaTabValues)

    NavHost(navController = navController, startDestination = startDestination) {

        composable<Home> { navBackStackEntry ->
            val home: Home = navBackStackEntry.toRoute()
            HomeScreen(
                tabs = tabs,
                selectedTab = home.tab,
                modifier = modifier
                    .fillMaxSize()
                    .padding(dimensionResource(id = R.dimen.padding_big))
            )
        }

        composable<SpellingDestination> { navBackStackEntry ->
            val spelling: SpellingDestination = navBackStackEntry.toRoute()
            SpellingRoute(
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() },
                addNewCallback = {
                    navController.navigate(
                        SpellingUploadDestination(
                            getSpellingUploadSheetAction(
                                spelling.sheetAction
                            )
                        )
                    )
                },
                windowSize = windowSizeClass,
                modifier = modifier,
            )
        }

        composable<ReadingDestination> {
            ReadingRoute(
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() },
                windowSize = windowSizeClass,
                modifier = modifier
            )
        }

        composable<SpellingUploadDestination> {
            SpellingUploadRoute(
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() },
                modifier = modifier
            )
        }

        composable<SentenceDestination> { navBackStackEntry ->
            val sentence: SentenceDestination = navBackStackEntry.toRoute()
            val titleId = if (sentence.sheetAction == SheetAction.READ_HOMOPHONES) {
                R.string.homophones_title
            } else {
                R.string.verbs_title
            }

            FillInSentenceSentenceRoute(
                titleId = titleId,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() },
                modifier = modifier
            )
        }

        composable<SpanishDestination> { navBackStackEntry ->
            val spanish: SpanishDestination = navBackStackEntry.toRoute()
            val titleId = if (spanish.sheetAction == SheetAction.READ_ZITA_SPANISH_WORDS ||
                spanish.sheetAction == SheetAction.READ_SPANISH_WORDS
            ) {
                R.string.spanish_title
            } else {
                R.string.spanish_week_title
            }

            SpanishRoute(
                titleId = titleId,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() },
                windowSize = windowSizeClass,
                modifier = modifier
            )
        }

        composable<SpanishUploadDestination> { navBackStackEntry ->
            SpanishUploadRoute(
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() },
                modifier = modifier
            )
        }
    }
}

private fun getSpellingUploadSheetAction(currentSheetAction: SheetAction): SheetAction {
    return when (currentSheetAction) {
        SheetAction.READ_ERIK_SPELLING_WORDS -> SheetAction.SAVE_ERIK_WORDS
        SheetAction.READ_MARK_SPELLING_WORDS -> SheetAction.SAVE_MARK_WORDS
        else -> SheetAction.SAVE_ERIK_WORDS
    }
}