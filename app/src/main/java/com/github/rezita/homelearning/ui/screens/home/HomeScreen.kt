package com.github.rezita.homelearning.ui.screens.home

import android.content.res.Configuration
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.ui.screens.common.LearningAppBar
import com.github.rezita.homelearning.ui.theme.HomeLearningTheme

sealed class Tabs(val tabName: String) {
    data object ErikTab : Tabs("Erik")
    data object MarkTab : Tabs("Mark")
}

data class TabButton(
    val titleId: Int,
    val onClick: () -> Unit
)

@Composable
fun HomeScreen(
    onClickErikSpelling: () -> Unit = {},
    onClickIrregularVerbs: () -> Unit = {},
    onClickHomophones: () -> Unit = {},
    onClickErikUpload: () -> Unit = {},
    onClickReading: () -> Unit = {},
    onClickReadingCEW: () -> Unit = {},
    onClickMarkSpelling: () -> Unit = {},
    onClickMarkUpload: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val tabItems = listOf(Tabs.ErikTab, Tabs.MarkTab)
    var selectedTab by remember { mutableStateOf(0) }
    Scaffold(
        topBar = {
            LearningAppBar(
                titleText = stringResource(id = R.string.app_name),
                isNavigateUp = false
            )
        }
    ) {
        Column(
            modifier = modifier
                .padding(it)
                .fillMaxWidth()
        )
        {
            TabRow(
                selectedTabIndex = selectedTab,

                ) {
                tabItems.forEachIndexed { index, it ->
                    Tab(
                        text = { Text(text = it.tabName) },
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                    )
                }
            }
            when (selectedTab) {
                0 -> ErikTab(
                    onClickSpelling = onClickErikSpelling,
                    onClickIrregularVerbs = onClickIrregularVerbs,
                    onClickHomophones = onClickHomophones,
                    onClickUpload = onClickErikUpload,
                )

                1 -> MarkTab(
                    onClickReading = onClickReading,
                    onClickReadingCEW = onClickReadingCEW,
                    onClickSpelling = onClickMarkSpelling,
                    onClickUpload = onClickMarkUpload,
                )
            }
        }
    }
}

@Composable
fun ErikTab(
    onClickSpelling: () -> Unit = {},
    onClickIrregularVerbs: () -> Unit = {},
    onClickHomophones: () -> Unit = {},
    onClickUpload: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val erikButtons = listOf(
        TabButton(
            titleId = R.string.start_erik_spelling,
            onClick = onClickSpelling
        ),
        TabButton(
            titleId = R.string.start_irregularVerbs,
            onClick = onClickIrregularVerbs
        ),
        TabButton(
            titleId = R.string.start_homophones,
            onClick = onClickHomophones
        ),
        TabButton(
            titleId = R.string.upload_erik_words,
            onClick = onClickUpload
        )
    )
    TabWithButtons(erikButtons, modifier)
}

@Composable
fun MarkTab(
    onClickReading: () -> Unit = {},
    onClickReadingCEW: () -> Unit = {},
    onClickSpelling: () -> Unit = {},
    onClickUpload: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val markButtons = listOf(
        TabButton(
            titleId = R.string.start_reading,
            onClick = onClickReading
        ),
        TabButton(
            titleId = R.string.reading_cew,
            onClick = onClickReadingCEW
        ),
        TabButton(
            titleId = R.string.start_mark_spelling,
            onClick = onClickSpelling
        ),
        TabButton(
            titleId = R.string.upload_mark_words,
            onClick = onClickUpload
        )
    )
    TabWithButtons(markButtons, modifier)
}

@Composable
private fun TabWithButtons(buttons: List<TabButton>, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = dimensionResource(id = R.dimen.padding_medium)),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(buttons) { button ->
            HomeLearningTabButton(
                title = stringResource(id = button.titleId),
                onClick = button.onClick
            )
        }
    }
}


@Composable
private fun HomeLearningTabButton(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick, modifier = modifier
            .fillMaxWidth(0.75f)
            .padding(
                top = dimensionResource(
                    id = R.dimen.padding_big
                )
            )
    ) {
        Text(text = title)
    }
}

@Composable
@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
fun MainTabsPreview() {
    HomeLearningTheme {
        HomeScreen()
    }
}

@Composable
@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
fun ErikTabPreview() {
    HomeLearningTheme {
        ErikTab()
    }
}

@Composable
@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
fun MarkTabPreview() {
    HomeLearningTheme {
        MarkTab()
    }
}