package com.github.rezita.homelearning.ui.screens.common.reading

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.github.rezita.homelearning.R

@Composable
fun ReadingContent(
    nrOfPages: Int,
    modifier: Modifier = Modifier,
    onPageChange: (Int) -> Unit = {},
    content: @Composable ColumnScope.(Int) -> Unit
) {
    val state = rememberPagerState { nrOfPages }
    HorizontalPager(
        state = state,
        modifier = modifier
            .fillMaxWidth()
    ) { page ->
        LaunchedEffect(Unit) {
            snapshotFlow { state.currentPage }.collect {
                onPageChange(it)
            }
        }

        ReadingWordItem(page, nrOfPages) {
            content(page)
        }
    }
}

@Composable
private fun ReadingWordItem(
    currentPageNr: Int,
    nrOfPages: Int,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        shape = MaterialTheme.shapes.small,
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        modifier = modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.padding_medium))
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .fillMaxHeight()
                .wrapContentSize(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        )
        {
            content()
        }
        Column(
            modifier = Modifier
                .padding(
                    end = dimensionResource(id = R.dimen.padding_medium),
                    bottom = dimensionResource(id = R.dimen.padding_medium)
                ),
            verticalArrangement = Arrangement.Bottom
        ) {
            Text(
                text = stringResource(R.string.reading_counter, currentPageNr + 1, nrOfPages),
                modifier = Modifier
                    .fillMaxWidth(),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Right,
            )
        }
    }
}