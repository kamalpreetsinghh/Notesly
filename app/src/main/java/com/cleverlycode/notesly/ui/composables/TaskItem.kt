package com.cleverlycode.notesly.ui.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import com.cleverlycode.notesly.ui.screens.notedetail.Task
import com.cleverlycode.notesly.ui.theme.AppTheme

@Composable
fun TaskItem(
    task: Task,
    taskId: Int,
    onCheckedChange: (Int, Boolean) -> Unit,
    onChange: (Int, String) -> Unit,
    onFocused: (Int) -> Unit,
    focusRequester: FocusRequester
) {
    val alpha = if (task.isVisible) 1f else 0f
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = AppTheme.dimens.horizontal_margin
            )
    ) {
        Checkbox(
            checked = task.isDone,
            onCheckedChange = { onCheckedChange(taskId, it) },
            modifier = Modifier
                .alpha(alpha = alpha)
                .focusRequester(focusRequester),
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colorScheme.onSurface
            )
        )
        TextField(
            value = task.name,
            onValueChange = { onChange(taskId, it) },
            modifier = Modifier
                .padding(start = AppTheme.dimens.horizontal_margin)
                .alpha(alpha = alpha)
                .onFocusChanged {
                    if (it.isFocused) {
                        onFocused(taskId)
                    }
                },
            textStyle = MaterialTheme.typography.bodyLarge,
            colors = TextFieldDefaults.textFieldColors(
                containerColor = MaterialTheme.colorScheme.background,
                cursorColor = MaterialTheme.colorScheme.primary,
                focusedIndicatorColor = MaterialTheme.colorScheme.background,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.background
            ),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                capitalization = KeyboardCapitalization.Sentences
            )
        )
    }
}