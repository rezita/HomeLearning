package com.github.rezita.homelearning.ui.screens.upload.common.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.github.rezita.homelearning.R

@Composable
fun UserInteractionIcons(
    onDeleteCallback: () -> Unit,
    onEditCallback: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        IconButton(onClick = onEditCallback) {
            Icon(
                imageVector = Icons.Outlined.Create,
                contentDescription = stringResource(id = R.string.upload_edit_word),
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
        IconButton(onClick = onDeleteCallback) {
            Icon(
                imageVector = Icons.Outlined.Delete,
                contentDescription = stringResource(id = R.string.upload_delete_word),
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}
