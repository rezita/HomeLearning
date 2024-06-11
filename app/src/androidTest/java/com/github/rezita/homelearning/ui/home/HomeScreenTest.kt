package com.github.rezita.homelearning.ui.home

import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import com.github.rezita.homelearning.ui.screens.home.HomeLearningTabItem
import com.github.rezita.homelearning.ui.screens.home.HomeScreen
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class HomeScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    /**
     * 1. Start HomeScreen without setting the selected tab
     * Exp. result: The first tab is selected
     */
    @Test
    fun HomeTabRow_init_without_selected_tab_test() {

        val tab1 = HomeLearningTabItem(
            name = "Erik",
            content = { Text(text = "Erik Tab") },
            onSelected = {}
        )
        val tab2 = HomeLearningTabItem(
            name = "Mark",
            content = { Text(text = "Mark Tab") },
            onSelected = {}
        )
        val testTabs = listOf(tab1, tab2)

        composeTestRule.setContent {
            HomeScreen(tabs = testTabs)
        }

        composeTestRule.onNodeWithText("Home Learning").assertIsDisplayed()
        composeTestRule.onNodeWithText("Erik Tab").assertIsDisplayed()
        composeTestRule
            .onNodeWithText(testTabs[0].name)
            .assertIsSelected()
    }

    /**
     * 1. Start HomeScreen (first tab is active)
     * Exp. result: The first tab is selected
     */
    @Test
    fun HomeTabRow_init_first_tab_selected_test() {

        val tab1 = HomeLearningTabItem(
            name = "Erik",
            content = { Text(text = "Erik Tab") },
            onSelected = { }
        )
        val tab2 = HomeLearningTabItem(
            name = "Mark",
            content = { Text(text = "Mark Tab") },
            onSelected = { }
        )
        val testTabs = listOf(tab1, tab2)

        composeTestRule.setContent {
            HomeScreen(tabs = testTabs, selectedTab = 0)
        }
//        composeTestRule.onRoot(useUnmergedTree = true).printToLog("tree")

        composeTestRule.onNodeWithText("Home Learning").assertIsDisplayed()
        composeTestRule.onNodeWithText("Erik Tab").assertIsDisplayed()
        composeTestRule
            .onNodeWithText(testTabs[0].name)
            .assertIsSelected()
    }


    /**
     * 1. Start HomeScreen with the second tab selected
     * Exp. result: The second tab is selected
     */
    @Test
    fun HomeTabRow_init_without_second_tab_selected_test() {

        val tab1 = HomeLearningTabItem(
            name = "Erik",
            content = { Text(text = "Erik Tab") },
            onSelected = {}
        )
        val tab2 = HomeLearningTabItem(
            name = "Mark",
            content = { Text(text = "Mark Tab") },
            onSelected = {}
        )
        val testTabs = listOf(tab1, tab2)

        composeTestRule.setContent {
            HomeScreen(tabs = testTabs, selectedTab = 1)
        }

        composeTestRule.onNodeWithText("Home Learning").assertIsDisplayed()
        composeTestRule.onNodeWithText("Mark Tab").assertIsDisplayed()
        composeTestRule
            .onNodeWithText(testTabs[1].name)
            .assertIsSelected()
    }


    /**
     * 1. Start HomeScreen (first tab is active)
     * 2. Click on the second tab
     * Result: Second tab's content appears
     * (the onSelected callback is called and invoked, recomposition happens)
     */
    @Test
    fun HomeTabRow_select_tab_test() {
        composeTestRule.setContent {
            var selectedTab by remember { mutableStateOf(0) }
            val tab1 = HomeLearningTabItem(
                name = "Erik",
                content = { Text(text = "Erik Tab") },
                onSelected = { selectedTab = 0 }
            )
            val tab2 = HomeLearningTabItem(
                name = "Mark",
                content = { Text(text = "Mark Tab") },
                onSelected = { selectedTab = 1 }
            )

            val testTabs = listOf(tab1, tab2)


            HomeScreen(tabs = testTabs, selectedTab = selectedTab)
        }
        composeTestRule.onNodeWithText("Home Learning").assertIsDisplayed()
        composeTestRule.onNodeWithText("Erik Tab").assertIsDisplayed()
        composeTestRule
            .onNodeWithText("Erik")
            .assertIsSelected()

        composeTestRule.onNodeWithText("Mark").performClick()
        composeTestRule.onNodeWithText("Home Learning").assertIsDisplayed()
        composeTestRule.onNodeWithText("Mark Tab").assertIsDisplayed()
        composeTestRule
            .onNodeWithText("Mark")
            .assertIsSelected()
    }

    /**
     * 1. Start HomeScreen (first tab is active)
     * 2. Click on the second tab
     * 3. Click on the first tab
     * Result: First tab's content appears
     */
    @Test
    fun HomeTabRow_select_tab_back_test() {
        composeTestRule.setContent {
            var selectedTab by remember { mutableStateOf(0) }
            val tab1 = HomeLearningTabItem(
                name = "Erik",
                content = { Text(text = "Erik Tab") },
                onSelected = { selectedTab = 0 }
            )
            val tab2 = HomeLearningTabItem(
                name = "Mark",
                content = { Text(text = "Mark Tab") },
                onSelected = { selectedTab = 1 }
            )
            val testTabs = listOf(tab1, tab2)

            HomeScreen(tabs = testTabs, selectedTab = selectedTab)
        }

        composeTestRule.onNodeWithText("Mark").performClick()
        composeTestRule.onNodeWithText("Erik").performClick()
        composeTestRule.onNodeWithText("Home Learning").assertIsDisplayed()
        composeTestRule.onNodeWithText("Erik Tab").assertIsDisplayed()
        composeTestRule
            .onNodeWithText("Erik")
            .assertIsSelected()
    }

    /**
     * 1. Start HomeScreen without empty tabs list
     * Exp. result: throws IllegalArgumentException
     */
    @Test
    fun HomeTabRow_empty_tabs_tab_test() {
        Assert.assertThrows(IllegalArgumentException::class.java)
        {
            composeTestRule.setContent {
                HomeScreen(tabs = emptyList())
            }
        }
    }

    /**
     * 1. Start HomeScreen with invalid selecetd index (out of bound)
     * Exp. result: throws IllegalArgumentException
     */
    @Test
    fun HomeTabRow_invalid_tabindex_test() {
        Assert.assertThrows(IllegalArgumentException::class.java)
        {
            composeTestRule.setContent {
                val tab1 = HomeLearningTabItem(
                    name = "Erik",
                    content = { Text(text = "Erik Tab") },
                    onSelected = { }
                )
                val tab2 = HomeLearningTabItem(
                    name = "Mark",
                    content = { Text(text = "Mark Tab") },
                    onSelected = { }
                )
                val testTabs = listOf(tab1, tab2)
                HomeScreen(tabs = testTabs, selectedTab = 2)
            }
        }
    }

    /**
     * 1. Start HomeScreen with invalid selecetd index (out of bound)
     * Exp. result: throws IllegalArgumentException
     */
    @Test
    fun HomeTabRow_negative_tabindex_test() {
        Assert.assertThrows(IllegalArgumentException::class.java)
        {
            composeTestRule.setContent {
                val tab1 = HomeLearningTabItem(
                    name = "Erik",
                    content = { Text(text = "Erik Tab") },
                    onSelected = { }
                )
                val tab2 = HomeLearningTabItem(
                    name = "Mark",
                    content = { Text(text = "Mark Tab") },
                    onSelected = { }
                )
                val testTabs = listOf(tab1, tab2)
                HomeScreen(tabs = testTabs, selectedTab = -1)
            }
        }
    }
}