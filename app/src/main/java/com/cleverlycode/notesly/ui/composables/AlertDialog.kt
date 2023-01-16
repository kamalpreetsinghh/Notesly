package com.cleverlycode.notesly.ui.composables

import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.cleverlycode.notesly.R

@Composable
fun AlertDialog(
    title: String,
    text: String,
    confirmButtonText: String,
    dismissButtonText: String,
    onDismiss: () -> Unit,
    confirmButtonClick: (() -> Unit) -> Unit,
    navigateToNotes: () -> Unit = {}
) {
    AlertDialog(
        modifier = Modifier.width(dimensionResource(id = R.dimen.alert_width)),
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineLarge
            )
        },
        text = {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        onDismissRequest = { onDismiss() },
        confirmButton = {
            TextButton(
                onClick = { confirmButtonClick(navigateToNotes) }
            ) {
                Text(
                    text = confirmButtonText,
                    style = MaterialTheme.typography.headlineLarge
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onDismiss() }
            ) {
                Text(
                    text = dismissButtonText,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.headlineLarge
                )
            }
        }
    )
}