package com.cleverlycode.notesly.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cleverlycode.notesly.ui.screens.notes.NoteType

@Composable
fun NoteTypesChips(
    noteTypes: List<NoteType>,
    selectedNoteType: String,
    onSelectionChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        items(noteTypes) { noteType ->
            Chip(
                label = noteType.value,
                modifier = Modifier,
                isSelected = noteType.value == selectedNoteType,
                onSelectionChanged = onSelectionChanged
            )
        }
    }
}