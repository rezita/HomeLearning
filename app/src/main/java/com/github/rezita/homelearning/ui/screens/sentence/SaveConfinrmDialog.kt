package com.github.rezita.homelearning.ui.screens.sentence

import android.content.res.Configuration
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.ui.theme.HomeLearningTheme

@Composable
fun SaveConfirmDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit
) {
    AlertDialog(
        icon = {
            Icon(
                imageVector = Icons.Outlined.Save,
                contentDescription = stringResource(id = R.string.confirm_save_title)
            )
        },
        title = {
            Text(text = stringResource(id = R.string.confirm_save_title))
        },
        text = {
            Text(text = stringResource(id = R.string.confirm_save_sentence))
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(onClick = { onConfirmation() }) {
                Text(text = stringResource(id = R.string.confirm_save_ok))
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text(text = stringResource(id = R.string.confirm_save_cancel))
            }
        }
    )

}


@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun SaveConfirmDialogPreview() {
    HomeLearningTheme {
        SaveConfirmDialog(onDismissRequest = { }, onConfirmation = {})
    }
}