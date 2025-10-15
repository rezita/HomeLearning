package com.github.rezita.homelearning.ui.screens.upload.common.components

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.model.Uploadable
import com.github.rezita.homelearning.ui.screens.upload.common.UploadUiState

@Composable
fun <T : Uploadable> UploadSavedContent(
    state: UploadUiState.Saved<T>,
    modifier: Modifier = Modifier,
    itemContent: @Composable (Pair<T, String>) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(
                vertical = dimensionResource(id = R.dimen.padding_extra_big),
                horizontal = dimensionResource(id = R.dimen.padding_medium)
            )
    ) {
        items(state.savingResult) { item ->
            itemContent(item)
            HorizontalDivider(
                modifier = Modifier
                    .height(1.dp)
                    .fillMaxHeight()
                    .fillMaxWidth(),
                color = MaterialTheme.colorScheme.surfaceVariant
            )
        }
    }
}