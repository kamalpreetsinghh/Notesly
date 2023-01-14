package com.cleverlycode.notesly.ui.composables

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.focus.FocusRequester
import com.cleverlycode.notesly.ui.screens.notedetail.Task

@Composable
fun Tasks(
    tasks: State<List<Task>>,
    onCheckedChange: (Int, Boolean) -> Unit,
    onChange: (Int, String) -> Unit,
    onFocused: (Int) -> Unit,
    focusRequester: FocusRequester
) {
    LazyColumn {
        itemsIndexed(tasks.value) { index, task ->
            TaskItem(
                task = task,
                taskId = index,
                onCheckedChange = onCheckedChange,
                onChange = onChange,
                onFocused = onFocused,
                focusRequester = focusRequester
            )
        }
    }
}