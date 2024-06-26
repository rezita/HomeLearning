package com.github.rezita.homelearning.ui.screens.home

import android.content.res.Configuration
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.ui.screens.common.LearningAppBar
import com.github.rezita.homelearning.ui.theme.HomeLearningTheme

data class HomeLearningTabItem(
    val name: String,
    val content: @Composable () -> Unit,
    val onSelected: () -> Unit
)

data class TabButton(
    @StringRes val titleId: Int,
    val onClick: () -> Unit
)

@Composable
fun HomeScreen(
    tabs: List<HomeLearningTabItem>,
    modifier: Modifier = Modifier,
    selectedTab: Int = 0
) {
    require(tabs.isNotEmpty()) { "Tabs cannot be empty" }
    require(selectedTab in tabs.indices) { "Invalid tab index" }

    Scaffold(
        topBar = {
            Column {
                LearningAppBar(
                    title = stringResource(id = R.string.app_name),
                    canNavigateBack = false,
                    navigateUp = { }
                )
                TabRow(
                    selectedTabIndex = selectedTab,
                ) {
                    tabs.forEachIndexed { index, it ->
                        Tab(
                            text = { Text(text = it.name) },
                            selected = selectedTab == index,
                            onClick = { it.onSelected() },
                        )
                    }
                }
            }
        }
    ) {
        Column(
            modifier = modifier
                .padding(it)
                .fillMaxWidth()
        ) {
            tabs[selectedTab].content()
        }
    }
}

@Composable
fun TabWithButtons(buttons: List<TabButton>, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = dimensionResource(id = R.dimen.padding_medium)),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(buttons) { button ->
            HomeLearningTabButton(
                button = button
            )
        }
    }
}


@Composable
private fun HomeLearningTabButton(
    button: TabButton,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = button.onClick, modifier = modifier
            .fillMaxWidth(0.75f)
            .padding(
                top = dimensionResource(
                    id = R.dimen.padding_big
                )
            )
    ) {
        Text(text = stringResource(id = button.titleId))
    }
}

@Composable
@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
fun MainTabsPreview() {
    HomeLearningTheme {
        val tabWithButtons = listOf(
            TabButton(
                titleId = R.string.start_erik_spelling,
                onClick = { }
            ),
            TabButton(
                titleId = R.string.start_irregularVerbs,
                onClick = { }
            ),
            TabButton(
                titleId = R.string.start_homophones,
                onClick = { }
            ),
            TabButton(
                titleId = R.string.upload_erik_words,
                onClick = { }
            ))
        HomeScreen(
            tabs = listOf(
                HomeLearningTabItem(
                    name = "Erik",
                    content = { TabWithButtons(tabWithButtons) },
                    onSelected = {}),
                HomeLearningTabItem(
                    name = "Mark",
                    content = { TabWithButtons(tabWithButtons) },
                    onSelected = {})
            ),
            selectedTab = 0
        )
    }
}